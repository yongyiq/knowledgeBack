package com.houyaozu.knowledge.server.Repository;

import com.houyaozu.knowledge.common.utils.RedisCache;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Builder
@RequiredArgsConstructor
@Component
public class RedisChatMemoryRepository implements ChatMemoryRepository {


    private final RedisCache redisCache;
    @Override
    public List<String> findConversationIds() {
        return List.of();
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        return redisCache.getCacheList(conversationId) == null ? List.of() : redisCache.getCacheList(conversationId);
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        redisCache.setCacheList(conversationId, messages);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        redisCache.deleteObject(conversationId);
    }
}
