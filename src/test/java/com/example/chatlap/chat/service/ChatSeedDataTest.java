package com.example.chatlap.chat.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.chatlap.chat.dto.ChatMessageResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(scripts = "/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ChatSeedDataTest {

	@Autowired
	private ChatMessageService chatMessageService;

	@Test
	void startupSeedsConversationForUserAAndUserB() {
		List<ChatMessageResponse> conversation = chatMessageService.getConversation("userA", "userB");

		assertThat(conversation).isNotEmpty();
		assertThat(conversation)
			.extracting(ChatMessageResponse::sender)
			.contains("userA", "userB");
	}
}
