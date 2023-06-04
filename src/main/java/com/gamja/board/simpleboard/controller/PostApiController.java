package com.gamja.board.simpleboard.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.dto.PostSaveRequestDto;
import com.gamja.board.simpleboard.dto.PostUpdateRequestDto;
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

	@PatchMapping("/members/{memberId}/posts/{postId}")
	public Long update(@PathVariable Long memberId, @PathVariable Long postId, @RequestBody @Valid PostUpdateRequestDto requestDto) {
		return postService.update(memberId, postId, requestDto);
	}

	@GetMapping("/posts/{postId}")
	public PostResponseDto findPost(@PathVariable Long postId) {
		return postService.findById(postId);
	}

	@DeleteMapping("/members/{memberId}/posts/{postId}")
	public void deletePost(@PathVariable Long memberId, @PathVariable Long postId) {
		postService.delete(memberId, postId);
	}
}
