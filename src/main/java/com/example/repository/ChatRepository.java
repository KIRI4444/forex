package com.example.repository;

import com.example.domain.chat.Chat;
import com.example.domain.user.User;
import com.example.web.dto.chat.ChatDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c " +
            "LEFT JOIN FETCH c.firstUser fu " +
            "LEFT JOIN FETCH c.secondUser su " +
            "LEFT JOIN FETCH fu.profile fup " +
            "LEFT JOIN FETCH su.profile sup " +
            "WHERE c.firstUser.id = :userId OR c.secondUser.id = :userId")
    Page<Chat> findUserChats(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT c FROM Chat c
    WHERE (c.firstUser.id = :user1Id AND c.secondUser.id = :user2Id)
       OR (c.firstUser.id = :user2Id AND c.secondUser.id = :user1Id)
""")
    Optional<Chat> findChatBetweenUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}
