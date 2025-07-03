package com.houyaozu.knowledge.server.listener;

import com.houyaozu.knowledge.pojo.DTO.ChatMessageDTO;
import com.houyaozu.knowledge.pojo.domain.ChatMessage;
import com.houyaozu.knowledge.server.config.ChatMessageRabbitConfig;
import com.houyaozu.knowledge.server.service.ChatMessageService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 聊天消息消费者
 * @ Author     ：侯耀祖
 * @ Description：消费聊天消息并持久化到数据库
 */
@Component
@Slf4j
public class ChatMessageConsumer {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final int MAX_RETRY_COUNT = 3;

    /**
     * 消费主队列消息
     */
    @RabbitListener(queues = ChatMessageRabbitConfig.CHAT_MESSAGE_QUEUE)
    public void handleChatMessage(ChatMessageDTO chatMessageDTO, Message message, Channel channel) {
        try {
            log.info("接收到聊天消息: userId={}, chatId={}, role={}", 
                    chatMessageDTO.getUserId(), chatMessageDTO.getChatId(), chatMessageDTO.getRole());

            // 转换为实体对象
            ChatMessage chatMessage = ChatMessage.builder()
                    .userId(chatMessageDTO.getUserId())
                    .chatId(chatMessageDTO.getChatId())
                    .role(chatMessageDTO.getRole())
                    .message(chatMessageDTO.getMessage())
                    .timestamp(chatMessageDTO.getTimestamp())
                    .build();

            // 保存到数据库
            boolean saved = chatMessageService.save(chatMessage);
            
            if (saved) {
                log.info("聊天消息保存成功: id={}", chatMessage.getId());
                // 手动确认消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                log.error("聊天消息保存失败");
                // 拒绝消息，发送到死信队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }

        } catch (Exception e) {
            log.error("处理聊天消息失败: {}", e.getMessage(), e);
            try {
                // 拒绝消息，发送到死信队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception ex) {
                log.error("拒绝消息失败: {}", ex.getMessage(), ex);
            }
        }
    }

    /**
     * 消费死信队列消息
     */
    @RabbitListener(queues = ChatMessageRabbitConfig.CHAT_MESSAGE_DLX_QUEUE)
    public void handleDeadLetterMessage(ChatMessageDTO chatMessageDTO, Message message, Channel channel) {
        try {
            log.warn("接收到死信队列消息: userId={}, chatId={}, retryCount={}", 
                    chatMessageDTO.getUserId(), chatMessageDTO.getChatId(), chatMessageDTO.getRetryCount());

            // 增加重试次数
            chatMessageDTO.setRetryCount(chatMessageDTO.getRetryCount() + 1);

            if (chatMessageDTO.getRetryCount() <= MAX_RETRY_COUNT) {
                log.info("重新发送消息到主队列: retryCount={}", chatMessageDTO.getRetryCount());
                
                // 重新发送到主队列
                rabbitTemplate.convertAndSend(
                        ChatMessageRabbitConfig.CHAT_MESSAGE_EXCHANGE,
                        ChatMessageRabbitConfig.CHAT_MESSAGE_ROUTING_KEY,
                        chatMessageDTO
                );
                
                // 确认死信消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                log.error("消息重试次数超过限制，丢弃消息: userId={}, chatId={}, retryCount={}", 
                        chatMessageDTO.getUserId(), chatMessageDTO.getChatId(), chatMessageDTO.getRetryCount());
                
                // 可以在这里实现其他处理逻辑，比如记录到错误日志表
                handleFailedMessage(chatMessageDTO);
                
                // 确认死信消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }

        } catch (Exception e) {
            log.error("处理死信消息失败: {}", e.getMessage(), e);
            try {
                // 确认消息，避免无限循环
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception ex) {
                log.error("确认死信消息失败: {}", ex.getMessage(), ex);
            }
        }
    }

    /**
     * 处理最终失败的消息
     */
    private void handleFailedMessage(ChatMessageDTO chatMessageDTO) {
        try {
            // 这里可以实现失败消息的处理逻辑
            // 比如：记录到错误日志表、发送告警邮件等
            log.error("最终处理失败的消息: userId={}, chatId={}, message={}", 
                    chatMessageDTO.getUserId(), chatMessageDTO.getChatId(), chatMessageDTO.getMessage());
            
            // 可以选择保存到错误日志表
            // errorLogService.saveErrorLog(chatMessageDTO);
            
        } catch (Exception e) {
            log.error("处理失败消息时发生异常: {}", e.getMessage(), e);
        }
    }
}
