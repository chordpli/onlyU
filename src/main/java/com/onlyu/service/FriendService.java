package com.onlyu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlyu.domain.dto.friends.FriendResponse;
import com.onlyu.domain.entity.Friends;
import com.onlyu.domain.entity.Member;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.repository.ChatRoomRepository;
import com.onlyu.repository.FriendRequestRepository;
import com.onlyu.repository.FriendsRepository;
import com.onlyu.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {

	private final MemberRepository memberRepository;

	private final FriendsRepository friendsRepository;
	private final FriendRequestRepository friendRequestRepository;
	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public void addRelation(Member requester, Member receiver) {
		Friends friends = Friends.of(requester, receiver);
		friendsRepository.save(friends);
	}

	@Transactional
	public List<FriendResponse> findMyFriends(Long memberNo) {
		Member currentMember = memberRepository.findById(memberNo).orElseThrow(
			() -> {
				throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
					ErrorCode.MEMBER_NOT_FOUND.getMessage());
			}
		);

		List<Member> friends = findFriendsByMember(currentMember);

		return friends.stream().map(member -> FriendResponse.of(member, hasChatRoom(member, currentMember)))
			.collect(Collectors.toList());
	}

	@Transactional
	List<Member> findFriendsByMember(Member member) {
		List<Friends> friendships = friendsRepository.findFriendshipsByMember(member);

		List<Member> friends = friendships.stream()
			.map(f -> f.getMember1().equals(member) ? f.getMember2() : f.getMember1())
			.collect(Collectors.toList());

		return friends;
	}

	@Transactional
	public boolean hasChatRoom(Member member1, Member member2) {
		return chatRoomRepository.existsByMembersRegardlessOfOrder(member1, member2);
	}
}
