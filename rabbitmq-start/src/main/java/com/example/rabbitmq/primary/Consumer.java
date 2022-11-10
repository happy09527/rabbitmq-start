package com.example.rabbitmq.primary;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.concurrent.TimeUnit;

/**
 * @author: ZhangX
 * @createDate: 2022/11/8
 * @description: channel.basicConsume(队列名字 / String, 是否自动应答 / boolean, 消费时的回调 / 接口类, 无法消费的回调 / 接口类);
 */
public class Consumer {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        receiver2();
    }

    /**
     * @author: ZhangX
     * @date: 2022/11/9 10:00
     * @description: 消息接收自动应答模式
     **/
    public static void receiver1() throws Exception {
        Channel channel = MqUtils.getChannel();
        // 接受消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

    /**
     * @author: ZhangX
     * @date: 2022/11/9 10:09
     * @param: []
     * @return: void
     * @description: 手动应答
     **/
    public static void receiver2() throws Exception {
        Channel channel = MqUtils.getChannel();
        // 接受消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            /**
             * @description: 手动应答模式下的确认
             **/
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };
        // 设置是否进行公平分发
        channel.basicQos(5);
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
        System.out.println("接收成功");
    }


}
