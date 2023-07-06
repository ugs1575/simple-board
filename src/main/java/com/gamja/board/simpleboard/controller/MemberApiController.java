package com.gamja.board.simpleboard.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamja.board.simpleboard.common.dto.ResultDto;
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
		Long saveId = memberService.save(requestDto.toServiceRequest());
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

	@GetMapping
	public ResponseEntity<ResultDto<List<MemberResponseDto>>> findMembers(@RequestParam(defaultValue = "") String name, Pageable pageable) {
		List<MemberResponseDto> members = memberService.findMembers(name, pageable);
		return ResponseEntity.ok(ResultDto.of(members.size(), members));
	}

	@DeleteMapping("/{memberId}")
	public ResponseEntity<Void> delete(@PathVariable Long memberId) {
		memberService.delete(memberId);
		return ResponseEntity.noContent().build();
	}

}
