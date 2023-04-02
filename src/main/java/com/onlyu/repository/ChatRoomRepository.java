package com.onlyu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlyu.domain.entity.ChatRoom;
import com.onlyu.domain.entity.Member;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	List<ChatRoom> findAllByMember1_MemberNo(Long memberNo);
	List<ChatRoom> findAllByMember2_MemberNo(Long memberNo);

	Optional<ChatRoom> findByMember1AndMember2(Member member1, Member member2);
}
