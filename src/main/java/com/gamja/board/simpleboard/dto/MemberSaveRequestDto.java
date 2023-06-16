package com.gamja.board.simpleboard.dto;

import javax.validation.constraints.NotBlank;

import com.gamja.board.simpleboard.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSaveRequestDto {

	@NotBlank(message = "회원 이름은 필수 값 입니다.")
	private String name;

	@NotBlank(message = "아이디는 필수 값 입니다.")
	private String account;

	@NotBlank(message = "비밀번호는 필수 입니다.")
	private String password;

	@Builder
	public MemberSaveRequestDto(String name, String account, String password) {
		this.name = name;
		this.account = account;
		this.password = password;
	}

	public Member toEntity(String encodedPassword) {
		return Member.builder()
				.name(name)
				.account(account)
				.password(encodedPassword)
				.build();
	}
}
