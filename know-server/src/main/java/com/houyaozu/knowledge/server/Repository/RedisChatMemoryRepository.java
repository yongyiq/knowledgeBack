package com.houyaozu.knowledge.server.Repository;

import com.houyaozu.knowledge.common.utils.RedisCache;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
