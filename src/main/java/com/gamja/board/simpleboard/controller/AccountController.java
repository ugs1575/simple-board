package com.gamja.board.simpleboard.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AccountController {

	private final MemberService memberService;

	@GetMapping("/login")
	public String login() {
		return "account/login";
	}

	@PostMapping("/register")
	public String register(@Valid MemberSaveRequestDto requestDto) {
		memberService.save(requestDto);
		return "account/register";
	}
}
