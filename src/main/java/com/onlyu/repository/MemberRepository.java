package com.onlyu.repository;

import com.onlyu.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Long, Member> {




}
