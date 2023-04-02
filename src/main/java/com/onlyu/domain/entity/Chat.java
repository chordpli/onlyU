package com.onlyu.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chatNo;

	@ManyToOne(fetch = FetchType.LAZY)
	private ChatRoom chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member sender;

	private String message;
	private LocalDateTime createdAt;

	public void sendMessage(String message) {
		this.message = message;
	}
}
