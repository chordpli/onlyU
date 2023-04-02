package com.onlyu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlyu.domain.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
