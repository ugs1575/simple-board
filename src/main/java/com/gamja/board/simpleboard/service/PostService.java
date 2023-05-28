package com.gamja.board.simpleboard.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.dto.PostSaveRequestDto;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.entity.Post;
import com.gamja.board.simpleboard.exception.NotFoundMemberException;
import com.gamja.board.simpleboard.repository.MemberRepository;
import com.gamja.board.simpleboard.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

	private final MemberRepository memberRepository;

	private final PostRepository postRepository;

	@Transactional
	public Long save(Long memberId, PostSaveRequestDto requestDto) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(NotFoundMemberException::new);

		Post post = postRepository.save(requestDto.toEntity());
		post.writePost(member);

		return post.getId();
	}
}
