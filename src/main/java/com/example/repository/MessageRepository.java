package com.example.repository;

import com.example.domain.Message.Message;
import com.example.domain.chat.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
                SELECT m FROM Message m
                LEFT JOIN FETCH m.sender s
                LEFT JOIN FETCH s.profile sp
                WHERE m.chat.id = :chatId
                ORDER BY m.createdAt DESC
            """)
    Page<Message> findByIdOrderByCreatedAtDesc(@Param("chatId") Long chatId, Pageable pageable);
}
