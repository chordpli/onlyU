package com.onlyu.controller;

import com.onlyu.domain.dto.member.LoginResponse;
import com.onlyu.service.FriendRequestService;
import com.onlyu.service.FriendService;
import com.onlyu.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequestMapping("/friend")
@Controller
@RequiredArgsConstructor
public class FriendController {

  private final MemberService memberService;
  private final FriendService friendService;
  private final FriendRequestService friendRequestService;


  /* Request */
  @PostMapping("/request")
  public String addFriend(@RequestParam(name = "memberNo") Long memberNo,
      @SessionAttribute(name = "loginUser", required = true) LoginResponse loginMember,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse) {

    friendRequestService.sendFriendRequest(loginMember.getMemberNo(), memberNo);

    return "redirect:/";
  }

  @PostMapping("/request/accept")
  public String acceptRequest(@RequestParam(name = "memberNo") Long memberNo,
      @SessionAttribute(name = "loginUser", required = true) LoginResponse loginMember,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse) {

    friendRequestService.decideFriendRequest(memberNo, loginMember.getMemberNo(), true);

    return "redirect:/";
  }

  // todo: 요청 거절
  @PostMapping("/request/refuse")
  public String refuseRequest(@RequestParam(name = "memberNo") Long memberNo,
      @SessionAttribute(name = "loginUser", required = true) LoginResponse loginMember,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse) {

    friendRequestService.decideFriendRequest(memberNo, loginMember.getMemberNo(), false);

    return "redirect:/";
  }

  // todo" 요청 리스트
}
