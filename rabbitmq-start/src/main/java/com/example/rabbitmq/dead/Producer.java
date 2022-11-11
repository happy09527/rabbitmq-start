package com.example.rabbitmq.dead;

import com.example.rabbitmq.primary.MqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MapRpcServer;

/**
 * @author: ZhangX
 * @createDate: 2022/11/10
 * @description:
 */
public class Producer {
    //普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws  Exception {
        Channel channel = MqUtils.getChannel();

        //死信消息 设置ttl时间 live to time 单位是ms
//        AMQP.BasicProperties properties =
//                new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 1; i <11 ; i++) {
            String message = "info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());
        }
    }
}
