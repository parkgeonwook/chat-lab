package com.example.chatlap.chat.controller;

import com.example.chatlap.chat.dto.ChatMessageRequest;
import com.example.chatlap.chat.dto.ChatMessageResponse;
import com.example.chatlap.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessageRequest request) {
        ChatMessageResponse savedMessage = chatMessageService.saveMessage(request);

        messagingTemplate.convertAndSend(
                "/queue/messages/" + savedMessage.receiver(),
                savedMessage
        );
    }
}
