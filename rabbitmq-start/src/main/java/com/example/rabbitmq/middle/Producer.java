package com.example.rabbitmq.middle;

import com.example.rabbitmq.primary.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 * @author: ZhangX
 * @createDate: 2022/11/9
 * @description: 发布确认模式，
 * 1、单个确认
 * 2、批量确认
 * 3、异步批量确认
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        producer3();
    }

    //    private static final String QUEUE_NAME = "hello";
    private static final int MESSAGE_COUNT = 1000;

    /**
     * @author: ZhangX
     * @date: 2022/11/9 15:58
     * @param: []
     * @return: void
     * @description: 单个确认 ，耗时45111ms
     **/
    public static void produce1() throws Exception {
        Channel channel = MqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "message " + i;
            channel.basicPublish("", queueName, null, message.getBytes());
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }

    /**
     * @author: ZhangX
     * @date: 2022/11/9 16:20
     * @param: []
     * @return: void
     * @description: 批量确认633ms
     **/
    public static void produce2() throws Exception {
        Channel channel = MqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "message " + i;
            channel.basicPublish("", queueName, null, message.getBytes());
            if ((i + 1) % 100 == 0) {
                boolean flag = channel.waitForConfirms();
                System.out.println("确认" + flag);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }

    /**
     * @author: ZhangX
     * @date: 2022/11/9 16:41
     * @param:
     * @return:
     * @description: 异步发布确认，消耗时间119ms
     **/
    public static void producer3() throws Exception {
        Channel channel = MqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.confirmSelect();
        channel.queueDeclare(queueName, true, false, false, null);
        long begin = System.currentTimeMillis();
        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况下
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序号
         * 3.支持高并发(多线程)
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms =
                new ConcurrentSkipListMap<>();
        //消息确认成功 ，两个参数 确认的消息 、是否批量确认
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                //2.删除掉已经确认的消息 剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息:" + deliveryTag);
        };
        //消息确认失败
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = outstandingConfirms.remove(deliveryTag);
            System.out.println("未确认的消息:" + message + deliveryTag);
        };
        // 信道监听器,监听那些消息成功了，哪些消息失败了
        channel.addConfirmListener(ackCallback, nackCallback);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "message " + i;
            channel.basicPublish("", queueName, null, message.getBytes());
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }
}
