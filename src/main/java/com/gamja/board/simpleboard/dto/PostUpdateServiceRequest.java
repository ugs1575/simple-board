package com.gamja.board.simpleboard.dto;

import javax.validation.constraints.NotBlank;

import com.gamja.board.simpleboard.entity.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateServiceRequest {

	private String title;
	private String content;

	@Builder
	private PostUpdateServiceRequest(String title, String content) {
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
