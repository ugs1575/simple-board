package com.gamja.board.simpleboard.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gamja.board.simpleboard.common.dto.ResultDto;
import com.gamja.board.simpleboard.dto.MemberResponseDto;
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
	public ResponseEntity<Void> save(@PathVariable Long memberId, @RequestBody @Valid PostSaveRequestDto requestDto) {
		Long saveId = postService.save(memberId, requestDto);
		return ResponseEntity.created(URI.create("/api/posts/" + saveId)).build();
	}

	@PatchMapping("/members/{memberId}/posts/{postId}")
	public ResponseEntity<Void> update(@PathVariable Long memberId, @PathVariable Long postId, @RequestBody @Valid PostUpdateRequestDto requestDto) {
		postService.update(memberId, postId, requestDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostResponseDto> findPost(@PathVariable Long postId) {
		postService.findById(postId);
		return ResponseEntity.ok(postService.findById(postId));
	}

	@GetMapping("/posts")
	public ResponseEntity<ResultDto<List<PostResponseDto>>> findPosts(Pageable pageable) {
		List<PostResponseDto> posts = postService.findPosts(pageable);
		return ResponseEntity.ok(ResultDto.of(posts.size(), posts));
	}

	@DeleteMapping("/members/{memberId}/posts/{postId}")
	public ResponseEntity<Void> deletePost(@PathVariable Long memberId, @PathVariable Long postId) {
		postService.delete(memberId, postId);
		return ResponseEntity.noContent().build();
	}
}
