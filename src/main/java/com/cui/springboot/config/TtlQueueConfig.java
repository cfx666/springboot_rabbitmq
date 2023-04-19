package com.cui.springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 队列交换机声明
 */
@Configuration
public class TtlQueueConfig {

    //普通交换机的名字
    public static final String X_EXCHANGE = "X";
    //死信交换机的名字
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列的名字
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    //死信队列的名字
    public static final String DEAD_LETTER_QUEUE = "QD";

    //声明普通交换机
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    //声明死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明普通队列 TTL 10s
    @Bean("queueA")
    public Queue queueA(){
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信routingkey
        arguments.put("x-dead-letter-routing-key","YD");
        //设置ttl  单位是ms
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //声明普通队列 TTL 40s
    @Bean("queueB")
    public Queue queueB(){
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","YD");
        //设置ttl  单位是ms
        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //声明普通队列QC
    @Bean("queueC")
    public Queue queueC(){
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    //声明死信队列
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //绑定
    @Bean()
    public Binding queueABingdingX(@Qualifier("queueA") Queue queueA,
                                   @Qualifier("xExchange") Exchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA").noargs();
    }

    //绑定
    @Bean()
    public Binding queueBBingdingX(@Qualifier("queueB") Queue queueB,
                                   @Qualifier("xExchange") Exchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB").noargs();
    }

    //绑定
    @Bean()
    public Binding queueDBingdingY(@Qualifier("queueD") Queue queueD,
                                   @Qualifier("yExchange") Exchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD").noargs();
    }

    //绑定
    @Bean()
    public Binding queueCBingdingX(@Qualifier("queueC") Queue queueC,
                                   @Qualifier("xExchange") Exchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC").noargs();
    }
}
