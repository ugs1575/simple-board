package com.gamja.board.simpleboard.dto;

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
}
