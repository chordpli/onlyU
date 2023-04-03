package com.onlyu.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.onlyu.domain.entity.Chat;
import com.onlyu.domain.entity.ChatRoom;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.repository.ChatRepository;
import com.onlyu.repository.ChatRoomRepository;
import com.onlyu.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final MemberRepository memberRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRepository chatRepository;

	public String addUser(Long roomNo, String sender) {
		return UUID.randomUUID().toString();
	}

	public void saveChat(Chat chat) {
		chatRepository.save(chat);
	}

	public List<Chat> getChatInfo(Long roomNo) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomNo).orElseThrow(()->{
			throw new OnlyUAppException(ErrorCode.NOT_FOUND_INFORMATION, ErrorCode.NOT_FOUND_INFORMATION.getMessage());
		});
		return chatRepository.findAllByChatRoom(chatRoom);
	}
}
