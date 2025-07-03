package com.houyaozu.knowledge.server.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 聊天消息RabbitMQ配置类
 * @ Author     ：侯耀祖
 * @ Description：配置聊天消息持久化的队列、交换机和死信队列
 */
@Configuration
public class ChatMessageRabbitConfig {

    // 主要的交换机、队列和路由键
    public static final String CHAT_MESSAGE_EXCHANGE = "chat.message.exchange";
    public static final String CHAT_MESSAGE_QUEUE = "chat.message.queue";
    public static final String CHAT_MESSAGE_ROUTING_KEY = "chat.message.save";

    // 死信交换机、队列和路由键
    public static final String CHAT_MESSAGE_DLX_EXCHANGE = "chat.message.dlx.exchange";
    public static final String CHAT_MESSAGE_DLX_QUEUE = "chat.message.dlx.queue";
    public static final String CHAT_MESSAGE_DLX_ROUTING_KEY = "chat.message.dlx";

    // TTL时间（毫秒）- 消息在死信队列中的存活时间
    public static final int MESSAGE_TTL = 60000; // 60秒

    /**
     * 主交换机
     */
    @Bean
    public DirectExchange chatMessageExchange() {
        return new DirectExchange(CHAT_MESSAGE_EXCHANGE, true, false);
    }

    /**
     * 主队列 - 配置死信交换机
     */
    @Bean
    public Queue chatMessageQueue() {
        return QueueBuilder.durable(CHAT_MESSAGE_QUEUE)
                .withArgument("x-dead-letter-exchange", CHAT_MESSAGE_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", CHAT_MESSAGE_DLX_ROUTING_KEY)
                .build();
    }

    /**
     * 主队列绑定
     */
    @Bean
    public Binding chatMessageBinding() {
        return BindingBuilder
                .bind(chatMessageQueue())
                .to(chatMessageExchange())
                .with(CHAT_MESSAGE_ROUTING_KEY);
    }

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange chatMessageDlxExchange() {
        return new DirectExchange(CHAT_MESSAGE_DLX_EXCHANGE, true, false);
    }

    /**
     * 死信队列 - 配置TTL
     */
    @Bean
    public Queue chatMessageDlxQueue() {
        return QueueBuilder.durable(CHAT_MESSAGE_DLX_QUEUE)
                .withArgument("x-message-ttl", MESSAGE_TTL)
                .withArgument("x-dead-letter-exchange", CHAT_MESSAGE_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", CHAT_MESSAGE_ROUTING_KEY)
                .build();
    }

    /**
     * 死信队列绑定
     */
    @Bean
    public Binding chatMessageDlxBinding() {
        return BindingBuilder
                .bind(chatMessageDlxQueue())
                .to(chatMessageDlxExchange())
                .with(CHAT_MESSAGE_DLX_ROUTING_KEY);
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        
        // 设置发送确认
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.err.println("消息发送失败: " + cause);
            }
        });
        
        // 设置返回确认
        rabbitTemplate.setReturnsCallback(returned -> {
            System.err.println("消息被退回: " + returned.getMessage());
        });
        
        return rabbitTemplate;
    }
}
