package com.onlyu.repository;

import com.onlyu.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  Page<Member> findAllByEmailIsContainingIgnoreCase(String keyword, Pageable pageable);


  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);
}
