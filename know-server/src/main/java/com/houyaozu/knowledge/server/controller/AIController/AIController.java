package com.houyaozu.knowledge.server.controller.AIController;


import com.houyaozu.knowledge.common.exception.KnowledgeException;
import com.houyaozu.knowledge.common.login.LoginUser;
import com.houyaozu.knowledge.common.login.LoginUserHolder;
import com.houyaozu.knowledge.common.result.ResultCodeEnum;
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
import java.util.Objects;

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
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (Objects.isNull(loginUser)){
            throw new KnowledgeException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        String userId = loginUser.getUserId().toString();
        chatHistoryRepository.saveHistory(userId, chatId);
        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId + ":" +chatId))
                .stream()
                .content();
    }
    @GetMapping("/history/{type}")
    public List<String> history(@PathVariable("type") String type) {
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (Objects.isNull(loginUser)){
            throw new KnowledgeException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        String userId = loginUser.getUserId().toString();
        return chatHistoryRepository.getChatIds(userId);
    }
    @GetMapping("/history/{type}/{chatId}")
    public List<MessageVO> history(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (Objects.isNull(loginUser)){
            throw new KnowledgeException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        String userId = loginUser.getUserId().toString();
        List<Message> messages = chatMemory.get(userId + ":" +chatId);
        if (messages.isEmpty()){
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }
}
