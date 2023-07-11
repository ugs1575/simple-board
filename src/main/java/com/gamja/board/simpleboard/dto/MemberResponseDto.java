package com.gamja.board.simpleboard.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.gamja.board.simpleboard.entity.Member;

import jdk.jshell.Snippet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {

	private Long id;
	private String name;

	@Builder
	private MemberResponseDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MemberResponseDto of(Member member) {
		return MemberResponseDto.builder()
			.id(member.getId())
			.name(member.getName())
			.build();
	}

	public static List<MemberResponseDto> listOf(List<Member> members) {
		return members.stream()
			.map(MemberResponseDto::of)
			.collect(Collectors.toList());
	}
}
