package com.gamja.board.simpleboard.repository;

import static com.gamja.board.simpleboard.entity.QMember.*;
import static com.gamja.board.simpleboard.entity.QPost.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.dto.PostSearchCondition;
import com.gamja.board.simpleboard.dto.QPostResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public PostRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<PostResponseDto> search(PostSearchCondition condition, Pageable pageable) {
		List<PostResponseDto> content = queryFactory
			.select(new QPostResponseDto(
				post.id.as("postId"),
				post.title,
				post.content,
				post.lastModifiedDate,
				member.id.as("memberId"),
				member.name.as("memberName")
			))
			.from(post)
			.join(post.member, member)
			.where(
				memberNameEq(condition.getMemberName()),
				keywordEq(condition.getKeyword())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(post.count())
			.from(post)
			.join(post.member, member)
			.where(
				memberNameEq(condition.getMemberName()),
				keywordEq(condition.getKeyword())
			);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private BooleanExpression keywordEq(String keyword) {
		return hasText(keyword) ?
			post.title.contains(keyword).or(post.content.contains(keyword)) : null;
	}

	private BooleanExpression memberNameEq(String memberName) {
		return hasText(memberName) ?
			member.name.contains(memberName) : null;
	}
}
