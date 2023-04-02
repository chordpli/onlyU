package com.onlyu.domain.dto.chat;

import com.onlyu.domain.entity.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChatUserResponse {
	private String member1Nickname;
	private String member2Nickname;

	public static ChatUserResponse fromEntity(ChatRoom chatRoom) {
		return ChatUserResponse.builder()
			.member1Nickname(chatRoom.getMember1().getNickname())
			.member2Nickname(chatRoom.getMember2().getNickname())
			.build();
	}
}
