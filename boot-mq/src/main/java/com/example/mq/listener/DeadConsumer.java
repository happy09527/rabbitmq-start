package com.example.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: ZhangX
 * @createDate: 2022/11/11
 * @description:
 */

@Component
@Slf4j
public class DeadConsumer {

    @RabbitListener(queues = "QD")
    public void receiveDead(Message message, Channel channel)throws Exception{
        String msg = new String(message.getBody());
        log.info("时间{}收到死信队列的消息{}",new Date(),msg);
    }
    @RabbitListener(queues = "QA")
    public void receiveLeave(Message message, Channel channel)throws Exception{
        String msg = new String(message.getBody());
        log.info("时间{}收到队列的消息{}",new Date(),msg);
    }
}