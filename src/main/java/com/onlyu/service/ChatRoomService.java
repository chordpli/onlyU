package com.onlyu.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onlyu.domain.entity.ChatRoom;
import com.onlyu.domain.entity.Member;
import com.onlyu.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;

	public ChatRoom createChatRoom(Member member1, Member member2) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.inviteMembers(member1, member2);
		return chatRoomRepository.save(chatRoom);
	}

}
