package com.cui.springboot.controller;

import com.cui.springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMsg (@PathVariable("message") String msg){

        /*rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY,msg);
        log.info("发送消息内容：" + msg);*/

        String tempMsg = msg;
        //指定消息ID为1
        //结论：发送到交换机，交换机返回成功信息，会调用成功的回调，消费者会消费该消息(key1)
        CorrelationData correlationData1 = new CorrelationData("1");
        msg = msg + "交换机名字confirm.exchange:::key1";
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY,msg,correlationData1);
        log.info("发送消息内容：" + msg);

        msg = tempMsg;
        //指定消息ID为2 交换机错误，key正确
        //结论：发送到交换机，交换机返回失败信息，会调用失败的回调
        CorrelationData correlationData2 = new CorrelationData("2");
        msg = msg + "交换机名字confirm.exchange123:::key1";
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME + "123",ConfirmConfig.CONFIRM_ROUTING_KEY,msg,correlationData2);
        log.info("发送消息内容：" + msg);

        msg = tempMsg;
        //指定消息ID为3  交换机正确，key错误
        //结论：发送到交换机，交换机返回成功信息，会调用成功的回调，消费者不会消费该消息(key3)
        CorrelationData correlationData3 = new CorrelationData("3");
        msg = msg + "交换机名字confirm.exchange:::key13";
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY + "3",msg,correlationData3);
        log.info("发送消息内容：" + msg);
    }
}
