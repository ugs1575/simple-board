package com.gamja.board.simpleboard.dto;

import javax.validation.constraints.NotBlank;

import com.gamja.board.simpleboard.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateServiceRequest {

	private String name;

	@Builder
	private MemberUpdateServiceRequest(String name) {
		this.name = name;
	}

	public Member toEntity() {
		return Member.builder()
			.name(name)
			.build();
	}
}
