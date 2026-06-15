package com.example.chatlap.chat.service;

import com.example.chatlap.chat.domain.ChatMessage;
import com.example.chatlap.chat.dto.ChatMessageRequest;
import com.example.chatlap.chat.dto.ChatMessageResponse;
import com.example.chatlap.chat.repository.ChatMessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public ChatMessageResponse saveMessage(ChatMessageRequest request) {
		ChatMessage chatMessage = ChatMessage.builder()
			.sender(request.sender())
			.receiver(request.receiver())
			.content(request.content())
			.createdAt(LocalDateTime.now())
			.build();

		ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
		return toResponse(savedMessage);
	}

	public List<ChatMessageResponse> getConversation(String currentUser, String partnerUser) {
		return chatMessageRepository.findConversation(currentUser, partnerUser).stream()
			.map(this::toResponse)
			.toList();
	}

	private ChatMessageResponse toResponse(ChatMessage chatMessage) {
		return new ChatMessageResponse(
			chatMessage.getId(),
			chatMessage.getSender(),
			chatMessage.getReceiver(),
			chatMessage.getContent(),
			chatMessage.getCreatedAt()
		);
	}
}
