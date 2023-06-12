package com.gamja.board.simpleboard.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.dto.PostForm;
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
	public void save(Long memberId, PostForm form) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		postRepository.save(form.toEntity(member));
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

	@Transactional
	public void update(Long postId, PostForm form) {
		Post post = postRepository.findByIdFetchJoin(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		post.update(form.getTitle(), form.getContent());
	}

	public PostResponseDto findById(Long postId) {
		Post post = postRepository.findByIdFetchJoin(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		return PostResponseDto.of(post);
	}

	public List<PostResponseDto> findPosts(Pageable pageable) {
		List<Post> posts = postRepository.findAll(pageable).getContent();

		return PostResponseDto.listOf(posts);
	}

	public Page<Post> findPostList(Pageable pageable) {
		return postRepository.findAll(pageable);
	}

	@Transactional
	public void delete(Long memberId, Long postId) {
		Post post = postRepository.findByIdFetchJoin(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Member author = post.getMember();
		if (!author.equals(member)) {
			throw new CustomException(ErrorCode.POST_UNAUTHORIZED);
		}

		postRepository.deleteById(postId);
	}
}
