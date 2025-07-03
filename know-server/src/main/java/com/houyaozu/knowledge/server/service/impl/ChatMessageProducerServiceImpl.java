package com.houyaozu.knowledge.server.service.impl;

import com.houyaozu.knowledge.pojo.DTO.ChatMessageDTO;
import com.houyaozu.knowledge.server.config.AutoMessage;
import com.houyaozu.knowledge.server.config.ChatMessageRabbitConfig;
import com.houyaozu.knowledge.server.config.RedisCache;
import com.houyaozu.knowledge.server.service.ChatMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 聊天消息生产者服务实现
 * @ Author     ：侯耀祖
 * @ Description：负责发送聊天消息到RabbitMQ队列
 */
@Service
@Slf4j
public class ChatMessageProducerServiceImpl implements ChatMessageProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void sendChatMessage(ChatMessageDTO chatMessageDTO) {
        try {
            log.info("发送聊天消息到队列: userId={}, chatId={}, role={}", 
                    chatMessageDTO.getUserId(), chatMessageDTO.getChatId(), chatMessageDTO.getRole());
            
            rabbitTemplate.convertAndSend(
                    ChatMessageRabbitConfig.CHAT_MESSAGE_EXCHANGE,
                    ChatMessageRabbitConfig.CHAT_MESSAGE_ROUTING_KEY,
                    chatMessageDTO
            );
            
            log.info("聊天消息发送成功");
        } catch (Exception e) {
            log.error("发送聊天消息失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendBatchChatMessages(String userId, String chatId) {
        try {
            String conversationId = userId + ":" + chatId;
            List<AutoMessage> autoMessages = redisCache.getCacheList(conversationId);
            
            if (autoMessages == null || autoMessages.isEmpty()) {
                log.warn("没有找到聊天记录: conversationId={}", conversationId);
                return;
            }

            log.info("开始批量发送聊天消息: conversationId={}, 消息数量={}", conversationId, autoMessages.size());

            for (AutoMessage autoMessage : autoMessages) {
                ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                        .userId(userId)
                        .chatId(chatId)
                        .role(autoMessage.getType())
                        .message(autoMessage.getText())
                        .timestamp(autoMessage.getTimestamp())
                        .build();

                sendChatMessage(chatMessageDTO);
            }

            log.info("批量发送聊天消息完成: conversationId={}", conversationId);
        } catch (Exception e) {
            log.error("批量发送聊天消息失败: userId={}, chatId={}, error={}", userId, chatId, e.getMessage(), e);
        }
    }
}
