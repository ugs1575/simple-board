package com.gamja.board.simpleboard.dto;

import javax.validation.constraints.NotBlank;

import com.gamja.board.simpleboard.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSaveRequestDto {

	@NotBlank(message = "회원 이름은 필수 입니다.")
	private String name;

	@Builder
	private MemberSaveRequestDto(String name) {
		this.name = name;
	}


	public MemberSaveServiceRequest toServiceRequest() {
		return MemberSaveServiceRequest.builder()
			.name(name)
			.build();
	}
}
