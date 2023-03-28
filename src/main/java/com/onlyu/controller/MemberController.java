package com.onlyu.controller;

import com.onlyu.domain.Response;
import com.onlyu.domain.dto.member.LoginRequest;
import com.onlyu.domain.dto.member.JoinRequest;
import com.onlyu.domain.dto.member.JoinResponse;
import com.onlyu.domain.dto.member.LoginResponse;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/authentication")
@RequiredArgsConstructor
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

  @PostMapping("/authenticate")
  public String authenticate(@RequestBody final @Valid LoginRequest request,
      BindingResult bindingResult,
      HttpServletRequest servletRequest,
      Model model,
      @RequestParam(required = false) String redirect) {

    if (bindingResult.hasErrors()) {
      return "redirect:/authentication/login";
    }

    try {
      LoginResponse response = memberService.authentication(request);

      HttpSession session = servletRequest.getSession();   //세션이 있으면 있는 세션 반환, 없으면 신규 세션
      session.setAttribute("loginUser", response);
    } catch (OnlyUAppException e) {
      throw new OnlyUAppException(ErrorCode.INCONSISTENT_INFORMATION,
          ErrorCode.INCONSISTENT_INFORMATION.getMessage());
    }

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
  public String join(@RequestBody final @Valid JoinRequest request) {
    try {
      memberService.join(request);
    } catch (DataIntegrityViolationException | OnlyUAppException e) {
      throw new OnlyUAppException(ErrorCode.DUPLICATED_MEMBER_INFO,
          ErrorCode.DUPLICATED_MEMBER_INFO.getMessage());
    } finally {
      return "redirect:/";
    }
  }
}
