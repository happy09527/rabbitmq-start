package com.example.rabbitmq.fanout;

import com.example.rabbitmq.primary.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: ZhangX
 * @createDate: 2022/11/9
 * @description:
 */
public class Consumer2 {
    public static void main(String[] args) throws Exception {
        consume();
    }

    private static final String EXCHANGE_NAME = "logs";

    public static void consume() throws Exception {
        Channel channel = MqUtils.getChannel();

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("控制台打印接收到的消息:" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });


    }
}
