package com.houyaozu.knowledge.server.controller.AIController;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@RestController
@RequestMapping("/AI")
public class AIController {
    private final ChatClient chatClient;
    public AIController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    @RequestMapping(value = "/chat", produces = "text/html;charset=UTF-8")
    public Flux<String> chat(String prompt){
        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content();
    }
}
