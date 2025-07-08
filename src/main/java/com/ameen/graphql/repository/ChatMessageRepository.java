package com.ameen.graphql.repository;

import com.ameen.graphql.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    @Query("SELECT m FROM ChatMessage m WHERE m.sender.id = :sender AND m.receiver.id = :receiver ORDER BY m.timestamp ASC")
    List<ChatMessage> findBySenderReceiver(Long sender, Long receiver);

}
