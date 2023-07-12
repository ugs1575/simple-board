package com.gamja.board.simpleboard.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gamja.board.simpleboard.common.domain.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(length = 500, nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private LocalDateTime registeredDateTime;

	@Builder
	private Post(String title, String content, LocalDateTime registeredDateTime, Member member) {
		this.title = title;
		this.content = content;
		this.registeredDateTime = registeredDateTime;
		this.member = member;
	}

	public void write(Member member, LocalDateTime registeredDateTime) {
		this.member = member;
		this.registeredDateTime = registeredDateTime;
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
