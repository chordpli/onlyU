package com.onlyu.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class Friends {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long friendNo;

  @ManyToOne
  @JoinColumn(name = "member1_no", referencedColumnName = "memberNo")
  private Member member1;

  @ManyToOne
  @JoinColumn(name = "member2_no", referencedColumnName = "memberNo")
  private Member member2;

  private LocalDateTime friendshipTime;

  public static Friends of(Member requester, Member receiver) {
    return Friends.builder()
        .member1(requester)
        .member2(receiver)
        .friendshipTime(LocalDateTime.now())
        .build();
  }
}
