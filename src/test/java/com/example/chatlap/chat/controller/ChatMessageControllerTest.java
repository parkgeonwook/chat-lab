package com.example.chatlap.chat.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.chatlap.chat.domain.ChatMessage;
import com.example.chatlap.chat.repository.ChatMessageRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ChatMessageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@BeforeEach
	void setUp() {
		chatMessageRepository.deleteAll();
		chatMessageRepository.save(ChatMessage.builder()
			.sender("userA")
			.receiver("userB")
			.content("hello")
			.createdAt(LocalDateTime.of(2026, 6, 15, 11, 0, 0))
			.build());
		chatMessageRepository.save(ChatMessage.builder()
			.sender("userB")
			.receiver("userA")
			.content("hi")
			.createdAt(LocalDateTime.of(2026, 6, 15, 11, 1, 0))
			.build());
		chatMessageRepository.save(ChatMessage.builder()
			.sender("userA")
			.receiver("userC")
			.content("ignore")
			.createdAt(LocalDateTime.of(2026, 6, 15, 11, 2, 0))
			.build());
	}

	@Test
	void getConversationReturnsConversationMessagesAsJson() throws Exception {
		mockMvc.perform(get("/api/messages/conversation")
				.param("currentUser", "userA")
				.param("partnerUser", "userB"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].sender").value("userA"))
			.andExpect(jsonPath("$[0].content").value("hello"))
			.andExpect(jsonPath("$[1].sender").value("userB"))
			.andExpect(jsonPath("$[1].content").value("hi"));
	}

	@Test
	void createMessageSavesAndReturnsMessageAsJson() throws Exception {
		mockMvc.perform(post("/api/messages")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "sender": "userB",
					  "receiver": "userA",
					  "content": "rest save"
					}
					"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.sender").value("userB"))
			.andExpect(jsonPath("$.receiver").value("userA"))
			.andExpect(jsonPath("$.content").value("rest save"))
			.andExpect(jsonPath("$.createdAt").exists());
	}
}
