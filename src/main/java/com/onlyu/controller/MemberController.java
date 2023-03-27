package com.onlyu.controller;

import com.onlyu.domain.Response;
import com.onlyu.domain.dto.member.JoinRequest;
import com.onlyu.domain.dto.member.JoinResponse;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/join")
  public Response<JoinResponse> join(@RequestBody final @Valid JoinRequest request){
    try {
      return Response.success(memberService.join(request));
    } catch (DataIntegrityViolationException | OnlyUAppException e) {
      throw new OnlyUAppException(ErrorCode.DUPLICATED_MEMBER_INFO, ErrorCode.DUPLICATED_MEMBER_INFO.getMessage());
    }
  }
}
