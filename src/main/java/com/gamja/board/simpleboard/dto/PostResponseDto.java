package com.gamja.board.simpleboard.dto;

import com.gamja.board.simpleboard.entity.Post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PostResponseDto {

	private Long id;
	private String title;
	private String content;
	private MemberResponseDto member;

	public static PostResponseDto of(Post post) {
		return new PostResponseDto(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			MemberResponseDto.of(post.getMember())
		);
	}
}
