package com.gamja.board.simpleboard.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.dto.MemberUpdateRequestDto;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public Long save(MemberSaveRequestDto requestDto) {
		return memberRepository.save(requestDto.toEntity()).getId();
	}

	@Transactional
	public Long update(Long id, MemberUpdateRequestDto requestDto) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id = " + id));

		member.update(requestDto.getName());

		return id;
	}
}
