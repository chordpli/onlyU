package com.onlyu.service;

import com.onlyu.domain.dto.friends.FriendResponse;
import com.onlyu.domain.entity.Friends;
import com.onlyu.domain.entity.Member;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.repository.FriendRequestRepository;
import com.onlyu.repository.FriendsRepository;
import com.onlyu.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {

  private final MemberRepository memberRepository;

  private final FriendsRepository friendsRepository;
  private final FriendRequestRepository friendRequestRepository;


  public void addRelation(Member requester, Member receiver) {
    Friends friends = Friends.of(requester, receiver);
    friendsRepository.save(friends);
  }

  public List<FriendResponse> findMyFriends(Long memberNo) {
    Member currentMember = memberRepository.findById(memberNo).orElseThrow(
        () -> {
          throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
              ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    );

    List<Member> friends = findFriendsByMember(currentMember);
    return friends.stream().map(FriendResponse::of).collect(Collectors.toList());
  }

  List<Member> findFriendsByMember(Member member) {
    List<Friends> friendships = friendsRepository.findFriendshipsByMember(member);

    List<Member> friends = friendships.stream()
        .map(f -> f.getMember1().equals(member) ? f.getMember2() : f.getMember1())
        .collect(Collectors.toList());

    return friends;
  }
}
