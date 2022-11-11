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
public class Consumer1 {
    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = MqUtils.getChannel();
        //声明一个队列
        channel.queueDeclare("console",false,false,false,null);
        // 进行绑定，队列与交换机 参数为： 队列名称、交换机名称、routing key
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");
        //接收消息
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息:"+new String(message.getBody(),"UTF-8"));
        };
        //消费者取消消息时回调接口
        channel.basicConsume("console",true,deliverCallback,consumerTag -> {});

    }}
