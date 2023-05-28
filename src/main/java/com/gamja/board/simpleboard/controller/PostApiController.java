package com.gamja.board.simpleboard.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gamja.board.simpleboard.dto.PostSaveRequestDto;
import com.gamja.board.simpleboard.service.PostService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PostApiController {

	private final PostService postService;

	@PostMapping("/members/{memberId}/posts")
	@ResponseStatus(HttpStatus.CREATED)
	public Long save(@PathVariable Long memberId, @RequestBody @Valid PostSaveRequestDto requestDto) {
		return postService.save(memberId, requestDto);
	}
}
