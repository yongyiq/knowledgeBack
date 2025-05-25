package com.houyaozu.knowledge.pojo.VO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;
/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@NoArgsConstructor
@Data
public class MessageVO {
    private String role;
    private String content;
    public MessageVO (Message message){
        switch (message.getMessageType()) {
            case USER :
                role = "user";
                break;
            case ASSISTANT :
                role = "assistant";
                break;
            default:
                role = "";
                break;

        }
        this.content = message.getText();
    }
}
