package com.houyaozu.knowledge.server.controller.AIController;


import com.houyaozu.knowledge.common.utils.BeanCopyUtils;
import com.houyaozu.knowledge.pojo.VO.MessageVO;
import com.houyaozu.knowledge.server.service.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {
    private final ChatClient chatClient;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatMemory chatMemory;
    @RequestMapping(value = "/chat", produces = "text/html;charset=UTF-8")
    public Flux<String> chat(String prompt, String chatId){
        chatHistoryRepository.saveHistory("chat", chatId);
        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }
    @GetMapping("/history/{type}")
    public List<String> history(@PathVariable("type") String type) {
        return chatHistoryRepository.getChatIds(type);
    }
    @GetMapping("/history/{type}/{chatId}")
    public List<MessageVO> history(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId);
        if (messages.isEmpty()){
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }
}
