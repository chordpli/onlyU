package com.onlyu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.onlyu.domain.dto.member.LoginResponse;
import com.onlyu.domain.entity.ChatRoom;
import com.onlyu.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatService chatService;

	// 채팅방 생성
	// 채팅방 생성 후 다시 / 로 return
	@PostMapping("/chat/createroom")
	public String createRoom(@SessionAttribute(name = "loginUser", required = true) LoginResponse loginMember,
		@RequestParam(name = "memberNo") Long memberNo,
		RedirectAttributes rttr) {

		ChatRoom room = chatService.createChatRoom(loginMember.getMemberNo(), memberNo);

		log.info("CREATE Chat Room {}", room);

		rttr.addFlashAttribute("roomInfo", room);
		return "redirect:/chat/room" + room.getRoomNo();
	}

	// 채팅방 입장 화면
	// 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
	// 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
	@GetMapping("/chat")
	public String roomDetail(@SessionAttribute(name = "loginUser", required = true) LoginResponse loginMember,
		@RequestParam(name = "memberNo") Long friendMemberNo,
		Model model) {

		log.info("friendMemberNo = {}", friendMemberNo);
		model.addAttribute("room", chatService.findRoomById(loginMember.getMemberNo(), friendMemberNo));
		return "pages/chat/chatroom";
	}
}
