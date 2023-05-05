package com.onlyu.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlyu.domain.entity.Chat;
import com.onlyu.domain.entity.ChatRoom;
import com.onlyu.service.ChatRoomService;
import com.onlyu.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

	private final SimpMessageSendingOperations template;
	private final ChatService chatService;
	private final ChatRoomService chatRoomService;

	// 해당 유저
	@MessageMapping("/chat/sendMessage")
	@Transactional
	public void sendMessage(@Payload Chat chat) {
		log.info("chat.getSender = {}, message = {}, roomNo = {}", chat.getSender(), chat.getMessage(),
			chat.getChatRoom().getRoomNo());
		ChatRoom chatRoom = chatRoomService.findRoomById(chat.getChatRoom().getRoomNo());
		chat.sendMessage(chat.getMessage(), chatRoom);
		chatService.saveChat(chat);
		template.convertAndSend("/sub/chat/room/" + chat.getChatRoom().getRoomNo(), chat);
	}

	@PostMapping("/chat/message/{roomNo}")
	@ResponseBody
	public List<Chat> getMessage(@PathVariable Long roomNo) {
		return chatService.getChatInfo(roomNo);
	}
}