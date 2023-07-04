package com.gamja.board.simpleboard.dto;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateForm {

	@NotBlank(message = "제목은 필수 입니다.")
	private String title;
	private String content;

	@Builder
	private PostUpdateForm(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public PostUpdateServiceRequest toServiceRequest() {
		return PostUpdateServiceRequest.builder()
			.title(title)
			.content(content)
			.build();
	}
}
