package com.onlyu.repository;

import java.util.List;
import java.util.Optional;

import com.onlyu.domain.entity.FriendRequest;
import com.onlyu.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

  Optional<FriendRequest> findByRequesterAndReceiver(Member requester, Member receiver);

  List<FriendRequest> findByReceiverAndStatus(Member receiver, FriendRequest.FriendRequestStatus status);
}
