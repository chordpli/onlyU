package com.onlyu.controller;

import com.onlyu.domain.dto.friends.FriendResponse;
import com.onlyu.domain.dto.member.LoginResponse;
import com.onlyu.service.FriendService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequestMapping
@Controller
@RequiredArgsConstructor
public class IndexController {

  private final FriendService friendService;

  @GetMapping
  public String index(@SessionAttribute(name = "loginUser", required = false) LoginResponse loginMember,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse,
      Model model){

    if (loginMember != null) {
      List<FriendResponse> myFriends = friendService.findMyFriends(loginMember.getMemberNo());
      model.addAttribute("friends", myFriends);
    }

    return "pages/index";
  }

}
