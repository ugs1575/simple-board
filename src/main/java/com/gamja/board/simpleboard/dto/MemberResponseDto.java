package com.gamja.board.simpleboard.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.gamja.board.simpleboard.entity.Member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberResponseDto {

	private Long id;
	private String name;

	public static MemberResponseDto of(Member member) {
		return new MemberResponseDto(member.getId(), member.getName());
	}

	public static List<MemberResponseDto> listOf(List<Member> members) {
		return members.stream()
			.map(MemberResponseDto::of)
			.collect(Collectors.toList());
	}
}
