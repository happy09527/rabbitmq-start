package com.example.mq.listener;

import com.example.mq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: ZhangX
 * @createDate: 2022/11/11
 * @description:
 */
@Slf4j
@Component
public class WarningConsumer {

    //接收报警信息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息:{}", msg);
    }
}