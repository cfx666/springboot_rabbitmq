package com.cui.springboot.consumer;

import com.cui.springboot.config.DelayedQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * 基于插件的延迟队列
 * 消费者
 */
@Component
@Slf4j
public class DelayQueueConsumer {

    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message) throws IOException {

        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延迟队列信息：{}", new Date().toString(), msg);

    }
}
