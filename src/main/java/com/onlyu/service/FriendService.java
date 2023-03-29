package com.onlyu.service;

import com.onlyu.domain.entity.FriendRequest;
import com.onlyu.domain.entity.Friends;
import com.onlyu.domain.entity.Member;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.repository.FriendRequestRepository;
import com.onlyu.repository.FriendsRepository;
import com.onlyu.repository.MemberRepository;
import java.time.LocalDateTime;
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
}
