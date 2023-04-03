package com.onlyu.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chatNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_no", referencedColumnName = "roomNo")
	private ChatRoom chatRoom;

	private String sender;

	private String message;

	@CreatedDate
	private LocalDateTime createdAt;

	public void sendMessage(String message, ChatRoom chatRoom) {
		this.message = message;
		this.chatRoom = chatRoom;
	}
}
