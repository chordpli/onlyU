package com.onlyu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlyu.domain.entity.Chat;
import com.onlyu.domain.entity.ChatRoom;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	List<Chat> findAllByChatRoom(ChatRoom chatRoom);
}
