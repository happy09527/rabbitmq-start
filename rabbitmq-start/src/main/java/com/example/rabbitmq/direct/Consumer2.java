package com.example.rabbitmq.direct;

import com.example.rabbitmq.primary.MqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: ZhangX
 * @createDate: 2022/11/9
 * @description:
 */
public class Consumer2 {

    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = MqUtils.getChannel();
        //声明一个direct交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("disk",false,false,false,null);
        channel.queueBind("disk",EXCHANGE_NAME,"error");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println("ReceiveLogsDirect02控制台打印接收到的消息:"+new String(message.getBody(),"UTF-8"));
        };
        //消费者取消消息时回调接口
        channel.basicConsume("disk",true,deliverCallback,consumerTag -> {});

    }
}
