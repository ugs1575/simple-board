package com.gamja.board.simpleboard.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gamja.board.simpleboard.dto.MemberResponseDto;
import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.dto.MemberUpdateRequestDto;
import com.gamja.board.simpleboard.service.MemberService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberApiController {

	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<Void> save(@RequestBody @Valid MemberSaveRequestDto requestDto) {
		Long saveId = memberService.save(requestDto);
		return ResponseEntity.created(URI.create("/api/members/" + saveId)).build();
	}

	@PatchMapping("/{memberId}")
	public ResponseEntity<Void> update(@PathVariable Long memberId, @RequestBody @Valid MemberUpdateRequestDto requestDto) {
		memberService.update(memberId, requestDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<MemberResponseDto> findMember(@PathVariable Long memberId) {
		return ResponseEntity.ok(memberService.findById(memberId));
	}

	@DeleteMapping("/{memberId}")
	public ResponseEntity<Void> delete(@PathVariable Long memberId) {
		memberService.delete(memberId);
		return ResponseEntity.noContent().build();
	}

}
