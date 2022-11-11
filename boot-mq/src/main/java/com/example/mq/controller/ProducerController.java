package com.example.mq.controller;

import com.example.mq.config.ConfirmConfig;
import com.example.mq.config.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
    public void send(@PathVariable("msg") String msg) {
        log.info("时间{}发送消息为：{}，", new Date(), msg);
        rabbitTemplate.convertAndSend("X", "XA", msg);
        rabbitTemplate.convertAndSend("X", "XB", msg);
    }

    /**
     * @author: ZhangX
     * @date: 2022/11/11 15:33
     * @param: [msg, ttl]
     * @return: void
     * @description: 优化后的延时队列。
     * 由发送端设置限制时间，到时后若无消费者接收，自动通过交换机转至死信队列
     **/
    @GetMapping("sendExpirationMsg/{msg}/{ttlTime}")
    public void sendTtl(@PathVariable("msg") String msg, @PathVariable("ttlTime") String ttlTime) {
        log.info("发送时间{}，内容{}", new Date(), msg);
        rabbitTemplate.convertAndSend("X", "XC", msg, message -> {
            //发送消息的时候 延迟时长
            message.getMessageProperties().setExpiration(ttlTime);
            return message;
        });
    }

    //开始发消息，基于插件的 消息及 延迟的时间
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable("message") String message,
                        @PathVariable("delayTime") Integer delayTime) {
        log.info("当前时间:{},发送一条时长是{}毫秒TTL信息给延迟队列delayed.queue：{}",
                new Date().toString(), delayTime, message);

        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
                    //发送消息的时候的延迟时长 单位ms
                    msg.getMessageProperties().setDelay(delayTime);
                    return msg;
                });
    }

    /**
     * @author: ZhangX
     * @date: 2022/11/11 19:38
     * @param:
     * @return:
     * @description: 高级消息确认
     * 发布测试
     **/
    @GetMapping("sendMsg/{msg}")
    public void testSendMsg(@PathVariable("msg") String msg) {
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, msg);
        log.info("发送消息内容为{}", msg);

    }
}
