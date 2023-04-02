package com.onlyu.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.onlyu.domain.dto.chat.ChatRoomResponse;
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

	public ChatRoom findRoomById(Long chatRoomNo) {
		return chatRoomRepository.findById(chatRoomNo).orElseThrow(() -> {
			throw new OnlyUAppException(ErrorCode.NOT_FOUND_INFORMATION, ErrorCode.NOT_FOUND_INFORMATION.getMessage());
		});
	}

	public ChatUserResponse getUserList(Long roomNo) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomNo)
			.orElseThrow(() -> {
				throw new OnlyUAppException(ErrorCode.NOT_FOUND_INFORMATION,
					ErrorCode.NOT_FOUND_INFORMATION.getMessage());
			});

		return ChatUserResponse.fromEntity(chatRoom);
	}

	public List<ChatRoomResponse> findMyChatRoomByMemberNo(Long memberNo) {
		return Stream.concat(chatRoomRepository.findAllByMember1_MemberNo(memberNo).stream(),
				chatRoomRepository.findAllByMember2_MemberNo(memberNo).stream())
			.filter(chatRoom -> chatRoom.getMember1().getMemberNo().equals(memberNo) ||
				chatRoom.getMember2().getMemberNo().equals(memberNo))
			.map(chatRoom -> {
				return ChatRoomResponse.of(chatRoom, memberNo);
			})
			.distinct()
			.collect(Collectors.toList());
	}
}
