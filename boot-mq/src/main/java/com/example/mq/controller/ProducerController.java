package com.example.mq.controller;

import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("ttl")
public class ProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * @author: ZhangX 
     * @date: 2022/11/11 11:11
     * @param: [msg]
     * @return: void
     * @description: 发消息路由
     **/
    @GetMapping("send/{msg}")
    public void send(@PathVariable("msg")String msg){
        log.info("时间{}发送消息为：{}，",System.currentTimeMillis(),msg);
        rabbitTemplate.convertAndSend("X","XA",msg);
        rabbitTemplate.convertAndSend("X","XB",msg);

    }
}
