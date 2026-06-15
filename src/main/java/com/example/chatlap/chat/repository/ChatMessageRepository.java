package com.example.chatlap.chat.repository;

import com.example.chatlap.chat.domain.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	@Query("""
		select message
		from ChatMessage message
		where (message.sender = :userA and message.receiver = :userB)
		   or (message.sender = :userB and message.receiver = :userA)
		order by message.createdAt asc, message.id asc
		""")
	List<ChatMessage> findConversation(
		@Param("userA") String userA,
		@Param("userB") String userB
	);
}
