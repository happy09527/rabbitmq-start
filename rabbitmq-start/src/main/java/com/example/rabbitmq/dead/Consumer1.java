package com.example.rabbitmq.dead;

import com.example.rabbitmq.primary.MqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ZhangX
 * @createDate: 2022/11/10
 * @description: 死信队列练习
 */
public class Consumer1 {
    //普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列的名称
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        consume();
    }
    public static void consume() throws Exception {

        Channel channel = MqUtils.getChannel();

        //声明死信和普通交换机，类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        //过期时间 10s 由生产者指定 更加灵活
        //arguments.put("x-message-ttl",10000);
        //正常的队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);//图中红箭头
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置正常队列长度的限制，例如发送10个消息，6个为正常，4个为死信
        arguments.put("x-max-length",6);

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定普通的交换机与队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");

        //绑定死信的交换机与死信的队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        System.out.println("等待接收消息...");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer01接受的消息是：" + new String(message.getBody(), "UTF-8"));
        };

        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, consumerTag -> {
        });
    }
}
