package com.gamja.board.simpleboard.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.dto.MemberResponseDto;
import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.dto.MemberSaveServiceRequest;
import com.gamja.board.simpleboard.dto.MemberUpdateRequestDto;
import com.gamja.board.simpleboard.dto.MemberUpdateServiceRequest;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.exception.CustomException;
import com.gamja.board.simpleboard.exception.ErrorCode;
import com.gamja.board.simpleboard.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public Long save(MemberSaveServiceRequest requestDto) {
		return memberRepository.save(requestDto.toEntity()).getId();
	}

	@Transactional
	public Long update(Long id, MemberUpdateServiceRequest requestDto) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		member.update(requestDto.getName());

		return id;
	}

	public MemberResponseDto findById(Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		return MemberResponseDto.of(member);
	}

	public List<MemberResponseDto> findMembers(String name, Pageable pageable) {
		List<Member> members = memberRepository.findByNameContainingIgnoreCase(name, pageable)
			.getContent();

		return MemberResponseDto.listOf(members);
	}

	@Transactional
	public void delete(Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		memberRepository.deleteById(id);
	}
}
