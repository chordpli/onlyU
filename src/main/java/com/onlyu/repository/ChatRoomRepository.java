package com.onlyu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlyu.domain.entity.ChatRoom;
import com.onlyu.domain.entity.Member;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	Optional<ChatRoom> findByMember1AndMember2(Member member1, Member member2);

}
