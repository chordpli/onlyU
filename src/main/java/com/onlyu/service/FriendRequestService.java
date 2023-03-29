package com.onlyu.service;

import com.onlyu.domain.entity.FriendRequest;
import com.onlyu.domain.entity.FriendRequest.FriendRequestStatus;
import com.onlyu.domain.entity.Member;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.repository.FriendRequestRepository;
import com.onlyu.repository.MemberRepository;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

  private final MemberRepository memberRepository;

  private final FriendService friendService;
  private final FriendRequestRepository friendRequestRepository;

  @Transactional
  public void sendFriendRequest(Long requesterNo, Long receiverNo) {

    Member requester = memberRepository.findById(requesterNo).orElseThrow(
        () -> {
          throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
              ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    );

    Member receiver = memberRepository.findById(receiverNo).orElseThrow(
        () -> {
          throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
              ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    );

    if (requester == receiver) {
      throw new IllegalArgumentException("You cannot send a friend request to yourself.");
    }

    FriendRequest existingRequest = friendRequestRepository.findByRequesterAndReceiver(requester,
        receiver);
    if (existingRequest != null) {
      throw new IllegalStateException("You have already sent a friend request to this user.");
    }

    FriendRequest friendRequest = FriendRequest.builder()
        .requester(requester)
        .receiver(receiver)
        .requestTime(LocalDateTime.now())
        .status(FriendRequestStatus.PENDING)
        .build();

    friendRequestRepository.save(friendRequest);
  }

  @Transactional
  public void acceptFriendRequest(Long receiverNo, Long requesterNo) {

    Member requester = memberRepository.findById(requesterNo).orElseThrow(
        () -> {
          throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
              ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    );

    Member receiver = memberRepository.findById(receiverNo).orElseThrow(
        () -> {
          throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
              ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    );

    FriendRequest existingRequest = friendRequestRepository.findByReceiver(receiver);

    if (existingRequest.getStatus().equals(FriendRequestStatus.REFUSE)) {
      throw new OnlyUAppException(ErrorCode.ALREADY_REFUSE_REQUEST,
          ErrorCode.ALREADY_REFUSE_REQUEST.getMessage());
    }

    if (existingRequest.getStatus().equals(FriendRequestStatus.ACCEPT)) {
      throw new OnlyUAppException(ErrorCode.ALREADY_ACCEPT_REQUEST,
          ErrorCode.ALREADY_ACCEPT_REQUEST.getMessage());
    }
    existingRequest.accept();
    friendRequestRepository.save(existingRequest);
    friendService.addRelation(requester, receiver);
  }

  @Transactional
  public void refuseFriendRequest(Long receiverNo, Long requesterNo) {

    Member requester = memberRepository.findById(requesterNo).orElseThrow(
        () -> {
          throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
              ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    );

    Member receiver = memberRepository.findById(receiverNo).orElseThrow(
        () -> {
          throw new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND,
              ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    );

    FriendRequest existingRequest = friendRequestRepository.findByReceiver(receiver);

    if (existingRequest.getStatus().equals(FriendRequestStatus.REFUSE)) {
      throw new OnlyUAppException(ErrorCode.ALREADY_REFUSE_REQUEST,
          ErrorCode.ALREADY_REFUSE_REQUEST.getMessage());
    }

    if (existingRequest.getStatus().equals(FriendRequestStatus.ACCEPT)) {
      throw new OnlyUAppException(ErrorCode.ALREADY_ACCEPT_REQUEST,
          ErrorCode.ALREADY_ACCEPT_REQUEST.getMessage());
    }

    existingRequest.refuse();
    friendRequestRepository.save(existingRequest);
  }


}
