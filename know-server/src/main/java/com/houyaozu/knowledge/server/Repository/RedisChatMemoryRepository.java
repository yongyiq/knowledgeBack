package com.houyaozu.knowledge.server.Repository;

import com.houyaozu.knowledge.server.config.RedisCache;
import com.houyaozu.knowledge.server.config.AutoMessage;
import lombok.Builder;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Builder
@Component
public class RedisChatMemoryRepository implements ChatMemoryRepository {


    private final RedisCache redisCache;
    public RedisChatMemoryRepository (RedisCache redisCache){
        this.redisCache = redisCache;
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

        // 追加新增消息
        for (AutoMessage msg : newMessages) {
            redisCache.addToList(conversationId, msg);
        }
    }



    @Override
    public void deleteByConversationId(String conversationId) {
        redisCache.deleteObject(conversationId);
    }
}
