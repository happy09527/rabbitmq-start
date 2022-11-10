package com.example.rabbitmq.primary;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author: ZhangX
 * @createDate: 2022/11/8
 * @description: 消息队列的生产者
 */
public class Producer {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();
        // 开启发布确认
        channel.confirmSelect();
        /**
         * @description: 生成一个队列
         * 参数设置：
         * 队列名称
         * 是否持久化，默认不持久化，消息默认存放在内存中，为不持久化
         * 是否只供一个消费者来使用，即消息是否共享，默认不共享
         * 是否自动删除
         *
         **/
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        String message = "hello world";
        /**
         * @description: 发送一个消息
         * 参数设置 ：
         * 交换机名称
         * 路由名称
         * 其他参数 消息是否持久化  MessageProperties.PERSISTENT_TEXT_PLAIN 参数为确认持久化
         * 要发送的消息
         **/
//        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//        System.out.println("消息发送完成");
        Scanner scanner = new Scanner(System.in);
//        while(true){
//            String s = scanner.nextLine();
//            channel.basicPublish("",QUEUE_NAME,null,s.getBytes());
//        }

        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, ("message +" + i).getBytes());
        }
    }
}
