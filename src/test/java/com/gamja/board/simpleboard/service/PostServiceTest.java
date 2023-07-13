package com.gamja.board.simpleboard.service;

import static com.gamja.board.simpleboard.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.gamja.board.simpleboard.IntegrationTestSupport;
import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.dto.PostSaveServiceRequest;
import com.gamja.board.simpleboard.dto.PostSearchCondition;
import com.gamja.board.simpleboard.dto.PostUpdateServiceRequest;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.entity.Post;
import com.gamja.board.simpleboard.exception.CustomException;
import com.gamja.board.simpleboard.repository.MemberRepository;
import com.gamja.board.simpleboard.repository.PostRepository;

class PostServiceTest extends IntegrationTestSupport {

	@Autowired
	private PostService postService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	@AfterEach
	void tearDown() {
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}
	
	@DisplayName("게시글 작성 후 게시글 아이디를 반환한다.")
	@Test
	void createPost() {
	    //given
		Member member = createMember("우경서");
		memberRepository.save(member);

		LocalDateTime now = LocalDateTime.now();

		PostSaveServiceRequest request = PostSaveServiceRequest.builder()
			.title("제목1")
			.content("내용1")
			.build();

		//when
		Long postId = postService.save(member.getId(), request, now);

		//then
		assertThat(postId).isNotNull();
		List<Post> posts = postRepository.findAll();
		assertThat(posts).hasSize(1)
			.extracting("title", "content", "registeredDateTime", "member.id")
			.containsExactly(
				tuple("제목1", "내용1", now, member.getId())
			);
	}

	@DisplayName("존재하지 않는 회원은 게시글 작성 시 예외가 발생한다.")
	@Test
	void createPostByNotExistedMember() {
		//given
		LocalDateTime now = LocalDateTime.now();

		PostSaveServiceRequest request = PostSaveServiceRequest.builder()
			.title("제목1")
			.content("내용1")
			.build();

		//when
		assertThatThrownBy(() -> postService.save(1L, request, now))
			.isInstanceOf(CustomException.class)
			.hasMessage(MEMBER_NOT_FOUND.getMessage());
	}

	@DisplayName("게시글 수정 후 게시글 아이디를 반환한다.")
	@Test
	void updatePost() {
	    //given
		Member member = createMember("우경서");
		Post post = createPost("제목1", "내용1", LocalDateTime.now(), member);

		memberRepository.save(member);
		postRepository.save(post);

		PostUpdateServiceRequest request = PostUpdateServiceRequest.builder()
			.title("제목_수정")
			.content("내용_수정")
			.build();

		//when
		Long postId = postService.update(member.getId(), post.getId(), request);

		//then
		assertThat(postId).isEqualTo(post.getId());
		List<Post> posts = postRepository.findAll();
		assertThat(posts).hasSize(1)
			.extracting("title", "content")
			.containsExactly(
				tuple("제목_수정", "내용_수정")
			);
	}

	@DisplayName("존재하지 않는 게시글은 게시글 수정 시 예외가 발생한다.")
	@Test
	void updatePostByNotExistedMember() {
		//given
		Member member = createMember("우경서");
		memberRepository.save(member);

		PostUpdateServiceRequest request = PostUpdateServiceRequest.builder()
			.title("제목_수정")
			.content("내용_수정")
			.build();

		//when, then
		assertThatThrownBy(() -> postService.update(member.getId(), 1L, request))
			.isInstanceOf(CustomException.class)
			.hasMessage(POST_NOT_FOUND.getMessage());
	}

	@DisplayName("작성자가 아닌 회원이 게시글 수정 시 예외가 발생한다.")
	@Test
	void updatePostByNotAuthor() {
		//given
		Member member1 = createMember("우경서");
		Member member2 = createMember("김경서");
		Post post = createPost("제목1", "내용1", LocalDateTime.now(), member1);

		memberRepository.saveAll(List.of(member1, member2));
		postRepository.save(post);

		PostUpdateServiceRequest request = PostUpdateServiceRequest.builder()
			.title("제목_수정")
			.content("내용_수정")
			.build();

		//when, then
		assertThatThrownBy(() -> postService.update(member2.getId(), post.getId(), request))
			.isInstanceOf(CustomException.class)
			.hasMessage(POST_UNAUTHORIZED.getMessage());
	}
	
	@DisplayName("게시글 아이디로 게시글을 조회한다.")
	@Test
	void findPostById() {
	    //given
		LocalDateTime now = LocalDateTime.now();

		Member member = createMember("우경서");
		Post post = createPost("제목1", "내용1", now, member);

		memberRepository.save(member);
		postRepository.save(post);
		
	    //when
		PostResponseDto findPost = postService.findById(post.getId());

		//then
		assertThat(findPost).extracting("postId", "title", "content", "registeredDateTime", "memberId", "memberName")
			.containsExactly(
				post.getId(), "제목1", "내용1", now, member.getId(), member.getName()
			);
	}

	@DisplayName("존재하지 않는 게시글 조회 시 예외가 발생한다.")
	@Test
	void findNotExistedPostById() {
		assertThatThrownBy(() -> postService.findById(1L))
			.isInstanceOf(CustomException.class)
			.hasMessage(POST_NOT_FOUND.getMessage());
	}

	@DisplayName("모든 게시물 목록을 반환한다.")
	@Test
	void findPosts() {
		//given
		LocalDateTime now = LocalDateTime.now();

		Member member1 = createMember("우경서");
		Post post1 = createPost("제목1", "내용1", now, member1);

		Member member2 = createMember("김경서");
		Post post2 = createPost("제목2", "내용2", now, member2);


		memberRepository.saveAll(List.of(member1, member2));
		postRepository.saveAll(List.of(post1, post2));

		PageRequest pageRequest = PageRequest.of(0, 3);

		//when
		List<PostResponseDto> posts = postService.findPosts(pageRequest);

		//then
		assertThat(posts).extracting("postId", "title", "content", "registeredDateTime", "memberId", "memberName")
			.containsExactly(
				tuple(post1.getId(), "제목1", "내용1", now, member1.getId(), member1.getName()),
				tuple(post2.getId(), "제목2", "내용2", now, member2.getId(), member2.getName())
			);
	}

	@DisplayName("저장된 게시글이 없을 경우 모든 게시물 조회 시 빈 객체를 반환한다.")
	@Test
	void findPostsNoResult() {
		//given
		PageRequest pageRequest = PageRequest.of(0, 3);

		//when
		List<PostResponseDto> posts = postService.findPosts(pageRequest);

		//then
		assertThat(posts).hasSize(0)
			.isEmpty();
	}

	@DisplayName("작성자 명, 제목 또는 글에 포함된 키워드로 검색 조건에 맞는 게시물을 조회한다.")
	@Test
	void searchPostsByAuthorNameAndKeyword() {
		//given
		LocalDateTime now = LocalDateTime.now();

		Member member1 = createMember("우경서");
		Post post1 = createPost("제목1", "내용1", now, member1);
		Post post2 = createPost("제목1", "내용2", now, member1);
		Post post3 = createPost("제목2", "내용3", now, member1);

		Member member2 = createMember("김경서");
		Post post4 = createPost("제목4", "내용4", now, member2);

		memberRepository.saveAll(List.of(member1, member2));
		postRepository.saveAll(List.of(post1, post2, post3, post4));

		PostSearchCondition searchCondition = PostSearchCondition.builder()
			.memberName("우경서")
			.keyword("2")
			.build();

		Pageable pageRequest = PageRequest.of(0, 3);

		//when
		Page<PostResponseDto> posts = postService.getBoard(searchCondition, pageRequest);

		//then
		assertThat(posts).hasSize(2)
			.extracting("postId")
			.containsExactlyInAnyOrder(
				post2.getId(), post3.getId()
			);

	}

	@DisplayName("작성자 명과 키워드로 검색된 결과가 없는 경우 빈 page 객체를 반환한다.")
	@Test
	void searchPostsByAuthorNameAndKeywordNoResult() {
		//given
		LocalDateTime now = LocalDateTime.now();

		Member member1 = createMember("우경서");
		Post post1 = createPost("제목1", "내용1", now, member1);
		Post post2 = createPost("제목1", "내용2", now, member1);
		Post post3 = createPost("제목2", "내용3", now, member1);

		Member member2 = createMember("김경서");
		Post post4 = createPost("제목4", "내용4", now, member2);

		memberRepository.saveAll(List.of(member1, member2));
		postRepository.saveAll(List.of(post1, post2, post3, post4));

		PostSearchCondition searchCondition = PostSearchCondition.builder()
			.memberName("박경서")
			.keyword("5")
			.build();

		Pageable pageRequest = PageRequest.of(0, 3);

		//when
		Page<PostResponseDto> posts = postService.getBoard(searchCondition, pageRequest);

		//then
		assertThat(posts).hasSize(0);
		assertThat(posts.getContent()).isEmpty();

	}

	@DisplayName("게시글 삭제한다.")
	@Test
	void deletePost() {
	    //given
		Member member = createMember("우경서");
		Post post = createPost("제목1", "내용1", LocalDateTime.now(), member);

		memberRepository.save(member);
		postRepository.save(post);

	    //when
		postService.delete(member.getId(), post.getId());

	    //then
		List<Post> posts = postRepository.findAll();
		assertThat(posts).hasSize(0)
			.isEmpty();
	}

	@DisplayName("게시글 작성자가 아닌 회원은 게시글을 삭제할 수 없다.")
	@Test
	void deletePostByNotAuthor() {
		//given
		Member member1 = createMember("우경서");
		Member member2 = createMember("김경서");
		Post post = createPost("제목1", "내용1", LocalDateTime.now(), member1);

		memberRepository.saveAll(List.of(member1, member2));
		postRepository.save(post);

		//when, then
		assertThatThrownBy(() -> postService.delete(member2.getId(), post.getId()))
			.isInstanceOf(CustomException.class)
			.hasMessage(POST_UNAUTHORIZED.getMessage());
	}

	private Member createMember(String name) {
		return Member.builder()
			.name(name)
			.build();
	}

	private Post createPost(String title, String content, LocalDateTime registeredDateTime, Member member) {
		return Post.builder()
			.title(title)
			.content(content)
			.registeredDateTime(registeredDateTime)
			.member(member)
			.build();
	}

}