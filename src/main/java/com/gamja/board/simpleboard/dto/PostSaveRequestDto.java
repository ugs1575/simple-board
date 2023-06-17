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


	@Builder
	public PostSaveRequestDto(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public Post toEntity() {
		return Post.builder()
			.title(title)
			.content(content)
			.build();
	}
}
