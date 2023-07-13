package com.gamja.board.simpleboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchCondition {
	private String memberName;
	private String keyword;

	@Builder
	private PostSearchCondition(String memberName, String keyword) {
		this.memberName = memberName;
		this.keyword = keyword;
	}
}
