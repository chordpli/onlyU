package com.onlyu.repository;

import com.onlyu.domain.entity.FriendRequest;
import com.onlyu.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

  FriendRequest findByRequesterAndReceiver(Member requester, Member receiver);

  FriendRequest findByReceiver(Member receiver);
}
