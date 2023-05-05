package com.onlyu.repository;

import com.onlyu.domain.entity.Friends;
import com.onlyu.domain.entity.Member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendsRepository extends JpaRepository<Friends, Long> {

	@Query("SELECT f FROM Friends f " +
		"JOIN f.member1 m1 " +
		"JOIN f.member2 m2 " +
		"WHERE (m1 = :member AND m2 != :member) OR (m1 != :member AND m2 = :member)")
	List<Friends> findFriendshipsByMember(@Param("member") Member member);

	@Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Friends f WHERE (f.member1 = :member1 AND f.member2 = :member2) OR (f.member1 = :member2 AND f.member2 = :member1)")
	boolean existsByFriendRegardlessOfOrder(@Param("member1") Member member1, @Param("member2") Member member2);
}