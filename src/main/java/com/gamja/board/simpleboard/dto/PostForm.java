package com.gamja.board.simpleboard.dto;

import javax.validation.constraints.NotBlank;

import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.entity.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostForm {

	@NotBlank(message = "제목은 필수 입니다.")
	private String title;
	private String content;

	@Builder
	private PostForm(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public PostSaveServiceRequest toServiceRequest() {
		return PostSaveServiceRequest.builder()
			.title(title)
			.content(content)
			.build();
	}
}
