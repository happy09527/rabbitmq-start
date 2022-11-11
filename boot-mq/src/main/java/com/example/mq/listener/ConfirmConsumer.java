package com.example.mq.listener;

import com.example.mq.config.ConfirmConfig;
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
public class ConfirmConsumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receive(Message message) {
        String msg = new String(message.getBody());
        log.info("时间{}收到确认队列的消息{}", new Date(), msg);
    }
}
