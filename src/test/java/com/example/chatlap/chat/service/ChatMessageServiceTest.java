package com.example.chatlap.chat.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.chatlap.chat.dto.ChatMessageRequest;
import com.example.chatlap.chat.dto.ChatMessageResponse;
import com.example.chatlap.chat.repository.ChatMessageRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatMessageServiceTest {

	@Autowired
	private ChatMessageService chatMessageService;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@BeforeEach
	void setUp() {
		chatMessageRepository.deleteAll();
	}

	@Test
	void saveMessageStoresSenderReceiverAndContent() {
		ChatMessageRequest request = new ChatMessageRequest("userA", "userB", "hello");

		ChatMessageResponse saved = chatMessageService.saveMessage(request);

		assertThat(saved.sender()).isEqualTo("userA");
		assertThat(saved.receiver()).isEqualTo("userB");
		assertThat(saved.content()).isEqualTo("hello");
		assertThat(saved.createdAt()).isNotNull();
		assertThat(chatMessageRepository.count()).isEqualTo(1);
	}

	@Test
	void getConversationReturnsSavedMessagesForTwoUsersOnly() {
		chatMessageService.saveMessage(new ChatMessageRequest("userA", "userB", "hello"));
		chatMessageService.saveMessage(new ChatMessageRequest("userB", "userA", "hi"));
		chatMessageService.saveMessage(new ChatMessageRequest("userA", "userC", "ignore"));

		List<ChatMessageResponse> conversation = chatMessageService.getConversation("userA", "userB");

		assertThat(conversation).hasSize(2);
		assertThat(conversation)
			.extracting(ChatMessageResponse::content)
			.
			containsExactly("hello", "hi");
	}
}
