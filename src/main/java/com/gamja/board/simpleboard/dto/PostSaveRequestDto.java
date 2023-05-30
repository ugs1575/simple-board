package com.gamja.board.simpleboard.dto;

import javax.validation.constraints.NotBlank;

import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.entity.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

	@NotBlank(message = "제목은 필수 입니다.")
	private String title;

	private String content;

	private Member member;


	@Builder
	public PostSaveRequestDto(String title, String content, Member member) {
		this.title = title;
		this.content = content;
		this.member = member;
	}

	public Post toEntity(Member member) {
		return Post.builder()
			.title(title)
			.content(content)
			.member(member)
			.build();
	}
}
