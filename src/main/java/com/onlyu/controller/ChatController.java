package com.onlyu.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.onlyu.domain.entity.Chat;
import com.onlyu.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

	private final SimpMessageSendingOperations template;
	private final ChatService chatService;
	// 해당 유저
	@MessageMapping("/chat/sendMessage")
	public void sendMessage(@Payload Chat chat) {
		log.info("CHAT {}", chat);
		chat.sendMessage(chat.getMessage());
		template.convertAndSend("/sub/chat/room/" + chat.getChatRoom().getRoomNo(), chat);
	}


}
