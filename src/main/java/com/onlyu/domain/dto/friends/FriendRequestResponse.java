package com.onlyu.domain.dto.friends;

import com.onlyu.domain.entity.FriendRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FriendRequestResponse {
	private Long friendRequestNo;
	private String requesterNickname;
	private String requesterEmail;

	public static FriendRequestResponse of(FriendRequest friendRequest) {
		return FriendRequestResponse.builder()
			.friendRequestNo(friendRequest.getFriendRequestNo())
			.requesterNickname(friendRequest.getRequester().getNickname())
			.requesterEmail(friendRequest.getRequester().getEmail())
			.build();
	}
}
