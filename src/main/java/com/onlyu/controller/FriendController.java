package com.onlyu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.onlyu.domain.dto.member.LoginResponse;
import com.onlyu.service.FriendRequestService;
import com.onlyu.service.FriendService;
import com.onlyu.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/friend")
@Controller
@RequiredArgsConstructor
@Slf4j
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
  public String acceptRequest(@RequestParam(name = "requestNo") Long requestNo,
      @SessionAttribute(name = "loginUser", required = true) LoginResponse loginMember,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse) {

    log.info("acceptRequest loginUser = {}", loginMember.getMemberNo());
    log.info("acceptRequest memberNo = {}", requestNo);
    friendRequestService.decideFriendRequest(requestNo, true);

    return "redirect:/";
  }

  // todo: 요청 거절
  @PostMapping("/request/refuse")
  public String refuseRequest(@RequestParam(name = "requestNo") Long requestNo,
      @SessionAttribute(name = "loginUser", required = true) LoginResponse loginMember,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse) {

    friendRequestService.decideFriendRequest(requestNo, false);

    return "redirect:/";
  }
}
