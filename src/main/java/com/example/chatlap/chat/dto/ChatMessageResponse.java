package com.example.chatlap.chat.dto;

import java.time.LocalDateTime;

public record ChatMessageResponse(
	Long id,
	String sender,
	String receiver,
	String content,
	LocalDateTime createdAt
) {
}
