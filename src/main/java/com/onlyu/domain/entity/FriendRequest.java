package com.onlyu.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FriendRequest {



  public enum FriendRequestStatus{
    PENDING, ACCEPT, REFUSE;
  }
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long friendRequestNo;

  @ManyToOne
  @JoinColumn(name = "requester_no", referencedColumnName = "memberNo")
  private Member requester;

  @ManyToOne
  @JoinColumn(name = "receiver_no", referencedColumnName = "memberNo")
  private Member receiver;

  private LocalDateTime requestTime;

  @Enumerated(EnumType.STRING)
  private FriendRequestStatus status;

  public void accept() {
    this.status = FriendRequestStatus.ACCEPT;
  }

  public void refuse() {
    this.status = FriendRequestStatus.REFUSE;
  }
}
