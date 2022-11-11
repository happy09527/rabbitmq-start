package com.example.mq.controller;

import com.example.mq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: ZhangX
 * @createDate: 2022/11/11
 * @description:
 */

@RestController
@Slf4j
@RequestMapping("confirm")
public class ConfirmController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("send/{message}")
    public void send(@PathVariable("message") String message) {
        //指定消息 id 为 1
        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, message + "key1", correlationData1);
        log.info("发送消息内容:{}", message + "key1");

        //指定消息 id 为 2
        CorrelationData correlationData2 = new CorrelationData("2");
        String CONFIRM_ROUTING_KEY = "key2";
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                CONFIRM_ROUTING_KEY, message + "key2", correlationData2);
        log.info("发送消息内容:{}", message + "key2");
    }

}
