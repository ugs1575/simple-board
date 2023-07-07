package com.gamja.board.simpleboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchCondition {
	private String author;
	private String keyword;

	@Builder
	private PostSearchCondition(String author, String keyword) {
		this.author = author;
		this.keyword = keyword;
	}
}
