package com.houyaozu.knowledge.server.Repository;

import com.houyaozu.knowledge.pojo.DTO.ChatMessageDTO;
import com.houyaozu.knowledge.server.config.RedisCache;
import com.houyaozu.knowledge.server.config.AutoMessage;
import com.houyaozu.knowledge.server.service.ChatMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ Author     ：侯耀祖
 * @ Description：Redis聊天记忆仓库，集成RabbitMQ异步持久化
 */
@Component
@Slf4j
public class RedisChatMemoryRepository implements ChatMemoryRepository {

    private final RedisCache redisCache;
    private final ChatMessageProducerService chatMessageProducerService;

    public RedisChatMemoryRepository(RedisCache redisCache, ChatMessageProducerService chatMessageProducerService) {
        this.redisCache = redisCache;
        this.chatMessageProducerService = chatMessageProducerService;
    }
//    Map<String, List<Message>> chatMemoryStore = new ConcurrentHashMap();
    @Override
    public List<String> findConversationIds() {
        return List.of();
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        List<AutoMessage> autoMessages = redisCache.getCacheList(conversationId);
        if (autoMessages == null) return List.of();

        return autoMessages.stream()
            .map(AutoMessage::toSpringMessage)
            .toList();
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        try {
            log.debug("保存聊天消息到Redis: conversationId={}, 消息数量={}", conversationId, messages.size());

            // 取出已有消息
            List<AutoMessage> existing = redisCache.getCacheList(conversationId);
            if (existing == null) {
                existing = List.of();
            }
            Set<String> existingKeys = existing.stream()
                .map(AutoMessage::getId)  // 定义消息唯一标识
                .collect(Collectors.toSet());

            // 过滤出新增消息
            List<AutoMessage> newMessages = messages.stream()
                .map(AutoMessage::fromSpringMessage)
                .filter(m -> !existingKeys.contains(m.getId()))
                .toList();

            // 追加新增消息到Redis
            for (AutoMessage msg : newMessages) {
                redisCache.addToList(conversationId, msg);
            }

            // 异步发送新增消息到RabbitMQ进行数据库持久化
            if (!newMessages.isEmpty()) {
                sendMessagesToQueue(conversationId, newMessages);
            }

            log.debug("聊天消息保存完成: conversationId={}, 新增消息数量={}", conversationId, newMessages.size());
        } catch (Exception e) {
            log.error("保存聊天消息失败: conversationId={}, error={}", conversationId, e.getMessage(), e);
        }
    }

    /**
     * 发送消息到RabbitMQ队列
     */
    private void sendMessagesToQueue(String conversationId, List<AutoMessage> newMessages) {
        try {
            // 解析conversationId获取userId和chatId
            String[] parts = conversationId.split(":", 2);
            if (parts.length != 2) {
                log.warn("无效的conversationId格式: {}", conversationId);
                return;
            }

            String userId = parts[0];
            String chatId = parts[1];

            // 发送每条新消息到队列
            for (AutoMessage autoMessage : newMessages) {
                ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                        .userId(userId)
                        .chatId(chatId)
                        .role(autoMessage.getType())
                        .message(autoMessage.getText())
                        .timestamp(autoMessage.getTimestamp())
                        .build();

                chatMessageProducerService.sendChatMessage(chatMessageDTO);
            }

            log.debug("消息已发送到队列: userId={}, chatId={}, 消息数量={}", userId, chatId, newMessages.size());
        } catch (Exception e) {
            log.error("发送消息到队列失败: conversationId={}, error={}", conversationId, e.getMessage(), e);
        }
    }



    @Override
    public void deleteByConversationId(String conversationId) {
        redisCache.deleteObject(conversationId);
    }
}
