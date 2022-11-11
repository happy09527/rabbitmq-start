package com.example.rabbitmq.dead;

import com.example.rabbitmq.primary.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: ZhangX
 * @createDate: 2022/11/10
 * @description:
 */
public class Consumer2 {
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        consume();
    }

    public static void consume() throws Exception {
        Channel channel = MqUtils.getChannel();

        System.out.println("等待接收死信消息...");

        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("Consumer02接受的消息是："+new String(message.getBody(),"UTF-8"));
        };

        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag -> {});
    }
}
