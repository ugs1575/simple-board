package com.gamja.board.simpleboard.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.gamja.board.simpleboard.entity.Post;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PostResponseDto {

	private Long postId;
	private String title;
	private String content;
	private LocalDateTime modifiedDate;
	private Long memberId;
	private String memberName;

	@QueryProjection
	public PostResponseDto(Long postId, String title, String content, LocalDateTime modifiedDate, Long memberId,
		String memberName) {
		this.postId = postId;
		this.title = title;
		this.content = content;
		this.modifiedDate = modifiedDate;
		this.memberId = memberId;
		this.memberName = memberName;
	}

	public static PostResponseDto of(Post post) {
		return new PostResponseDto(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getLastModifiedDate(),
			post.getMember().getId(),
			post.getMember().getName()
		);
	}

	public static List<PostResponseDto> listOf(List<Post> posts) {
		return posts.stream()
			.map(PostResponseDto::of)
			.collect(Collectors.toList());
	}
}
