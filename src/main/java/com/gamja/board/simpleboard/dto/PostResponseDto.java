package com.gamja.board.simpleboard.dto;

import com.gamja.board.simpleboard.entity.Post;

import lombok.Getter;

@Getter
public class PostResponseDto {

	private Long id;
	private String title;
	private String content;
	private MemberResponseDto member;

	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.member = new MemberResponseDto(post.getMember());
	}
}
