package com.onlyu.service;

import org.springframework.stereotype.Service;

import com.onlyu.domain.dto.chat.ChatUserResponse;
import com.onlyu.domain.entity.ChatRoom;
import com.onlyu.domain.entity.Member;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.repository.ChatRoomRepository;
import com.onlyu.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;

	public ChatRoom createChatRoom(Long myMemberNo, Long friendMemberNo) {
		Member member1 = memberRepository.findById(myMemberNo).orElseThrow(
			() -> {
				throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
					ErrorCode.MEMBER_NOT_FOUND.getMessage());
			}
		);

		Member member2 = memberRepository.findById(friendMemberNo).orElseThrow(
			() -> {
				throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
					ErrorCode.MEMBER_NOT_FOUND.getMessage());
			}
		);

		ChatRoom chatRoom = ChatRoom.of(member1, member2);
		return chatRoomRepository.save(chatRoom);
	}

	public ChatRoom findRoomById(Long myMemberNo, Long friendMemberNo) {
		Member member1 = memberRepository.findById(myMemberNo).orElseThrow(
			() -> {
				throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
					ErrorCode.MEMBER_NOT_FOUND.getMessage());
			}
		);

		Member member2 = memberRepository.findById(friendMemberNo).orElseThrow(
			() -> {
				throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
					ErrorCode.MEMBER_NOT_FOUND.getMessage());
			}
		);

		return chatRoomRepository.findByMember1AndMember2(member1, member2).orElseThrow(() -> {
			throw new OnlyUAppException(ErrorCode.NOT_FOUND_INFORMATION, ErrorCode.NOT_FOUND_INFORMATION.getMessage());
		});
	}

	public ChatUserResponse getUserList(Long roomNo) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomNo)
			.orElseThrow(()->{
				throw new OnlyUAppException(ErrorCode.NOT_FOUND_INFORMATION, ErrorCode.NOT_FOUND_INFORMATION.getMessage());
			});

		return ChatUserResponse.fromEntity(chatRoom);
	}
}
