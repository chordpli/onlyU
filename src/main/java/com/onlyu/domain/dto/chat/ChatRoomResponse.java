package com.onlyu.domain.dto.chat;

import com.onlyu.domain.entity.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class ChatRoomResponse {

	private Long chatRoomNo;
	private Long chatRoomMemberNo;
	private String chatRoomMemberNickname;

	public static ChatRoomResponse of(ChatRoom chatRoom, Long memberNo) {
		return ChatRoomResponse.builder()
			.chatRoomNo(chatRoom.getRoomNo())
			.chatRoomMemberNickname(chatRoom.getMember1().getMemberNo().equals(memberNo)
				? chatRoom.getMember2().getNickname() : chatRoom.getMember1().getNickname())
			.chatRoomMemberNo(chatRoom.getMember1().getMemberNo().equals(memberNo)
				? chatRoom.getMember2().getMemberNo() : chatRoom.getMember1().getMemberNo())
			.build();
	}
}
