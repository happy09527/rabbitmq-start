package com.example.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: ZhangX
 * @createDate: 2022/11/11
 * @description: 交换机确认回调接口的重写
 */

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    //注入
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);

    }

    /**
     * 交换机不管是否收到消息的一个回调方法
     * 1. 发消息 交换机接收到了 回调
     *
     * @param correlationData 保存回调信息的Id及相关信息
     * @param b               交换机收到消息 为true
     * @param s               未收到消息的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (b) {
            log.info("交换机接收消息成功，消息为{}", id);
        } else {
            log.info("交换机还未收到ID为:{}的消息，由于原因:{}", id, s);
        }
    }
    //可以在当消息传递过程中不可达目的地时将消息返回给生产者
    //只有不可达目的地的时候 才进行回退

    /**
     * 当消息无法路由的时候的回调方法
     * message      消息
     * replyCode    编码
     * replyText    退回原因
     * exchange     从哪个交换机退回
     * routingKey   通过哪个路由 key 退回
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        log.error("消息{},被交换机{}退回，退回原因:{},路由key:{}",
                new String(message.getBody()), i,
                s1, s2);
    }
}
