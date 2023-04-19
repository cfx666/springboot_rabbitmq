package com.cui.springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列插件
 */
@Configuration
public class DelayedQueueConfig {

    //队列名字
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    //声明队列
    @Bean("delayedQueue")
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //声明交换机
    @Bean("delayedExchange")
    public CustomExchange delayedExchange(){

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");

        /**
         * 1.交换机名字
         * 2.交换机类型
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,arguments);
    }

    //绑定
    @Bean()
    public Binding delayedQueueBingdingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                   @Qualifier("delayedExchange") Exchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
