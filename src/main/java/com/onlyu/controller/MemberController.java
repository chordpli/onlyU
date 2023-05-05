package com.onlyu.controller;

import com.onlyu.domain.dto.member.JoinRequest;
import com.onlyu.domain.dto.member.JoinResponse;
import com.onlyu.domain.dto.member.LoginRequest;
import com.onlyu.domain.dto.member.LoginResponse;
import com.onlyu.domain.dto.member.SearchResponse;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;


  @GetMapping("/login")
  public String loginForm(HttpServletRequest servletRequest, Model model,
      HttpServletResponse response) {
    HttpSession session = servletRequest.getSession(false);
    if (session != null) {
      LoginResponse loginResponse = (LoginResponse) session
          .getAttribute("loginUser");
      if (loginResponse != null) {
        return "redirect:/";
      }
      new SecurityContextLogoutHandler()
          .logout(servletRequest, response, SecurityContextHolder.getContext().getAuthentication());
    }
    String referrer = servletRequest.getHeader("Referer");

    model.addAttribute("request", new LoginRequest());
    model.addAttribute("referrer", referrer);
    return "pages/member/login";
  }

  @PostMapping("/logout")
  public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler()
        .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    return "redirect:/";
  }

  @PostMapping("/authenticate")
  public String authenticate(final @Valid LoginRequest request,
      BindingResult bindingResult,
      HttpServletRequest servletRequest,
      Model model,
      @RequestParam(required = false) String redirect) {

    if (bindingResult.hasErrors()) {
      return "redirect:/authentication/login";
    }

    try {
      LoginResponse response = memberService.authentication(request);
      log.info("login user ={}", response.getNickname());
      HttpSession session = servletRequest.getSession();   //세션이 있으면 있는 세션 반환, 없으면 신규 세션
      session.setAttribute("loginUser", response);
    } catch (OnlyUAppException e) {
      throw new OnlyUAppException(ErrorCode.INCONSISTENT_INFORMATION,
          ErrorCode.INCONSISTENT_INFORMATION.getMessage());
    }
    log.info("login redirect = {}", redirect);

    if (redirect == null) {
      return "redirect:/";
    } else {
      return "redirect:" + redirect;
    }
  }

  @GetMapping("/join")
  public String joinForm(HttpServletRequest servletRequest, Model model,
      HttpServletResponse response) {

    HttpSession session = servletRequest.getSession(false);

    if (session != null) {
      LoginResponse loginResponse = (LoginResponse) session
          .getAttribute("loginUser");
      if (loginResponse != null) {
        return "redirect:/";
      }
      new SecurityContextLogoutHandler()
          .logout(servletRequest, response, SecurityContextHolder.getContext().getAuthentication());
    }
    model.addAttribute("request", new JoinRequest());
    return "pages/member/join";
  }

  @PostMapping("/join")
  public String join(final @Valid JoinRequest request) {

    log.info("email = {}, nickname = {}", request.getEmail(), request.getNickname());

    try {
      JoinResponse member = memberService.join(request);
      log.info("JoinResponse no ={}, nickname = {}", member.getMemberNo(), member.getNickname());


    } catch (DataIntegrityViolationException | OnlyUAppException e) {
      throw new OnlyUAppException(ErrorCode.DUPLICATED_MEMBER_INFO,
          ErrorCode.DUPLICATED_MEMBER_INFO.getMessage());
    } finally {
      return "redirect:/";
    }
  }

  @GetMapping("/search")
  public String findMember(@RequestParam("keyword") String keyword,
      HttpServletRequest servletRequest,
      Model model,
      HttpServletResponse servletResponse,
      @SessionAttribute(name = "loginUser", required = true) LoginResponse loginMember) {

    Pageable pageable = PageRequest.of(0, 10, Sort.by("nickname").descending());
    Page<SearchResponse> members = memberService.findMember(loginMember.getMemberNo(), keyword, pageable);

    model.addAttribute("members", members);

    return "pages/member/find";

  }
}
