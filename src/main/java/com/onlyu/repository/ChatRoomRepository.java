package com.onlyu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onlyu.domain.entity.ChatRoom;
import com.onlyu.domain.entity.Member;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	List<ChatRoom> findAllByMember1_MemberNo(Long memberNo);

	List<ChatRoom> findAllByMember2_MemberNo(Long memberNo);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ChatRoom c WHERE (c.member1 = :member1 AND c.member2 = :member2) OR (c.member1 = :member2 AND c.member2 = :member1)")
	boolean existsByMembersRegardlessOfOrder(@Param("member1") Member member1, @Param("member2") Member member2);
}
