package com.houyaozu.knowledge.server.config;

import com.houyaozu.knowledge.server.Repository.RedisChatMemoryRepository;
import com.houyaozu.knowledge.server.service.ChatMessageProducerService;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author     ：侯耀祖
 * @ Description：聊天记忆配置类
 */
@Configuration
public class ChatMemoryConfiguration {

    @Bean
    public RedisCache redisCache(){
        return new RedisCache();
    }

    @Bean
    public RedisChatMemoryRepository redisChatMemoryRepository(RedisCache redisCache, ChatMessageProducerService chatMessageProducerService) {
        return new RedisChatMemoryRepository(redisCache, chatMessageProducerService);
    }

    @Bean
    public ChatMemory chatMemory(RedisChatMemoryRepository repository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(10)
                .build();
    }
}
