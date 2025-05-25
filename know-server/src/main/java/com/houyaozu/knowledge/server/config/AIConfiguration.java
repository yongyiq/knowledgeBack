package com.houyaozu.knowledge.server.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Configuration
public class AIConfiguration {


    private final ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

    @Bean
    public ChatMemory getChatMemory() {
        return chatMemory;
    }

    @Bean
    public ChatClient chatClient(OllamaChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("你是一个热心，爱分享知识的机器人，你的名字叫小知，请你用小智的身份和语气回答")
                .defaultAdvisors(
                    SimpleLoggerAdvisor.builder().build(),
                    MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }
}
