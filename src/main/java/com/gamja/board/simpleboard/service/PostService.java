package com.gamja.board.simpleboard.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.dto.MemberResponseDto;
import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.dto.PostSaveRequestDto;
import com.gamja.board.simpleboard.dto.PostUpdateRequestDto;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.entity.Post;
import com.gamja.board.simpleboard.exception.CustomException;
import com.gamja.board.simpleboard.exception.ErrorCode;
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
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Post post = postRepository.save(requestDto.toEntity(member));

		return post.getId();
	}

	@Transactional
	public Long update(Long memberId, Long postId, PostUpdateRequestDto requestDto) {
		Post post = postRepository.findByIdFetchJoin(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Member author = post.getMember();
		if (!author.equals(member)) {
			throw new CustomException(ErrorCode.POST_UNAUTHORIZED);
		}

		post.update(requestDto.getTitle(), requestDto.getContent());

		return postId;
	}

	public PostResponseDto findById(Long postId) {
		Post post = postRepository.findByIdFetchJoin(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		return new PostResponseDto(post);
	}
}
