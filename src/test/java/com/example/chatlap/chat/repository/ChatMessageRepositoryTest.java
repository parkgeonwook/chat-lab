package com.example.chatlap.chat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.chatlap.chat.domain.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ChatMessageRepositoryTest {

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@BeforeEach
	void setUp() {
		chatMessageRepository.deleteAll();
	}

	@Test
	void findConversationReturnsBidirectionalMessagesOrderedByCreatedAt() {
		chatMessageRepository.save(ChatMessage.builder()
			.sender("userA")
			.receiver("userB")
			.content("first")
			.createdAt(LocalDateTime.of(2026, 6, 15, 10, 0, 0))
			.build());
		chatMessageRepository.save(ChatMessage.builder()
			.sender("userB")
			.receiver("userA")
			.content("second")
			.createdAt(LocalDateTime.of(2026, 6, 15, 10, 1, 0))
			.build());
		chatMessageRepository.save(ChatMessage.builder()
			.sender("userA")
			.receiver("userC")
			.content("ignored")
			.createdAt(LocalDateTime.of(2026, 6, 15, 10, 2, 0))
			.build());

		List<ChatMessage> conversation = chatMessageRepository
			.findConversation("userA", "userB");

		assertThat(conversation).hasSize(2);
		assertThat(conversation)
			.extracting(ChatMessage::getContent)
			.
			containsExactly("first", "second");
	}
}
