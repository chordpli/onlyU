package com.onlyu.controller;

import com.onlyu.domain.dto.chat.ChatRoomResponse;
import com.onlyu.domain.dto.friends.FriendRequestResponse;
import com.onlyu.domain.dto.friends.FriendResponse;
import com.onlyu.domain.dto.member.LoginResponse;
import com.onlyu.service.ChatRoomService;
import com.onlyu.service.FriendRequestService;
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
  private final FriendRequestService friendRequestService;
  private final ChatRoomService chatRoomService;

  @GetMapping
  public String index(@SessionAttribute(name = "loginUser", required = false) LoginResponse loginMember,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse,
      Model model){

    if (loginMember != null) {
      List<FriendResponse> myFriends = friendService.findMyFriends(loginMember.getMemberNo());
      model.addAttribute("friends", myFriends);

      //todo: 친구 요정 목록 보기
      List<FriendRequestResponse> requests = friendRequestService.getRequestList(loginMember.getMemberNo());
      model.addAttribute("requests", requests);

      //채팅 목록 보기
      List<ChatRoomResponse> chatRoomList = chatRoomService.findMyChatRoomByMemberNo(loginMember.getMemberNo());
      model.addAttribute("chatRooms", chatRoomList);

    }

    return "pages/index";
  }

}
