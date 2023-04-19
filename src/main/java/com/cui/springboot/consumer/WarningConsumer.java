package com.cui.springboot.consumer;

import com.cui.springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveD(Message message) throws IOException {

        String msg = new String(message.getBody());
        log.info("报警发现不可路由消息：{}", msg);

    }
}
