package com.houyaozu.knowledge.server.config;

import com.houyaozu.knowledge.server.Repository.RedisChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Configuration
public class ChatMemoryConfiguration {

    @Bean
    public RedisCache redisCache(){
        return new RedisCache();
    }
    @Bean
    public ChatMemory chatMemory(RedisChatMemoryRepository repository) {
    return MessageWindowChatMemory.builder()
            .chatMemoryRepository(repository)
            .maxMessages(10)
            .build();
    }
}
