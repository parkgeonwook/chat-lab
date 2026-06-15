package com.example.chatlap.chat.dto;

public record ChatMessageRequest(
	String sender,
	String receiver,
	String content
) {
}
