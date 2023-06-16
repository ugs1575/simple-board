package com.gamja.board.simpleboard.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.dto.MemberResponseDto;
import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.dto.MemberUpdateRequestDto;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.entity.MemberRole;
import com.gamja.board.simpleboard.exception.CustomException;
import com.gamja.board.simpleboard.exception.ErrorCode;
import com.gamja.board.simpleboard.repository.MemberRepository;
import com.gamja.board.simpleboard.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;


	@Transactional
	public Long save(MemberSaveRequestDto requestDto) {
		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

		Member member = requestDto.toEntity(encodedPassword);
		MemberRole.createMemberUserRole(member);

		member.assignUserRole();
		return memberRepository.save().getId();
	}

	@Transactional
	public Long update(Long id, MemberUpdateRequestDto requestDto) {
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
		List<Member> members = memberRepository.findByNameContaining(name, pageable)
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
