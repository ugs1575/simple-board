package com.gamja.board.simpleboard.dto;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {

	@NotBlank(message = "회원 이름은 필수 입니다.")
	private String name;

	@Builder
	private MemberUpdateRequestDto(String name) {
		this.name = name;
	}

	public MemberUpdateServiceRequest toServiceRequest() {
		return MemberUpdateServiceRequest.builder()
			.name(name)
			.build();
	}
}
