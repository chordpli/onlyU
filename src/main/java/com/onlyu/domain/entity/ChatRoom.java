package com.onlyu.domain.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

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
@Getter
@Builder
public class ChatRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long roomNo;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member1;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member2;

  private LocalDateTime createdAt;

}