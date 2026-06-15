package com.example.chatlap.chat.controller;

import com.example.chatlap.chat.dto.ChatMessageRequest;
import com.example.chatlap.chat.dto.ChatMessageResponse;
import com.example.chatlap.chat.service.ChatMessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

	@GetMapping("/conversation")
	public List<ChatMessageResponse> getConversation(
		@RequestParam String currentUser,
		@RequestParam String partnerUser
	) {
		return chatMessageService.getConversation(currentUser, partnerUser);
	}

	@PostMapping
	public ChatMessageResponse createMessage(@RequestBody ChatMessageRequest request) {
		return chatMessageService.saveMessage(request);
	}
}
