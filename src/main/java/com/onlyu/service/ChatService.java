package com.onlyu.service;

import org.springframework.stereotype.Service;

import com.onlyu.repository.ChatRoomRepository;
import com.onlyu.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final MemberRepository memberRepository;
	private final ChatRoomRepository chatRoomRepository;


}
