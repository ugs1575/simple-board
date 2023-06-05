package com.gamja.board.simpleboard.dto;

import java.util.List;
import java.util.stream.Collectors;

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

	public static List<PostResponseDto> listOf(List<Post> posts) {
		return posts.stream()
			.map(PostResponseDto::of)
			.collect(Collectors.toList());
	}
}
