package com.gamja.board.simpleboard.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.IntegrationTestSupport;
import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.dto.PostSearchCondition;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.entity.Post;

@Transactional
class PostRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	@DisplayName("게시글 id로 게시글을 작성자 정보와 함께 조회한다.")
	@Test
	void findByIdFetchJoin() {
	    //given
		Member member = createMember("우경서");
		Post post = createPost("제목1", "내용1", member);

		memberRepository.save(member);
		postRepository.save(post);

		Optional<Post> expect = Optional.of(post);

		//when
		Optional<Post> actual = postRepository.findByIdFetchJoin(member.getId());

		//then
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual).isEqualTo(expect);
		assertThat(actual.get().getMember().getName()).isEqualTo(expect.get().getMember().getName());
	}
	
	@DisplayName("작성자 명, 제목 또는 글에 포함된 키워드로 검색 조건에 맞는 게시물을 조회한다.")
	@Test
	void searchByAuthorAndKeyword() {
	    //given
		Member member1 = createMember("우경서");
		Post post1 = createPost("제목1", "내용1", member1);
		Post post2 = createPost("제목1", "내용2", member1);
		Post post3 = createPost("제목2", "내용3", member1);

		Member member2 = createMember("김경서");
		Post post4 = createPost("제목4", "내용4", member2);

		memberRepository.saveAll(List.of(member1, member2));
		postRepository.saveAll(List.of(post1, post2, post3, post4));

		PostSearchCondition searchCondition = PostSearchCondition.builder()
			.author("우경서")
			.keyword("2")
			.build();

		Pageable pageRequest = PageRequest.of(0, 3);

		//when
		Page<PostResponseDto> posts = postRepository.search(searchCondition, pageRequest);

		//then
		assertThat(posts).hasSize(2)
			.extracting("postId")
			.containsExactlyInAnyOrder(
				post2.getId(), post3.getId()
			);
	    
	}

	@DisplayName("작성자 명과 키워드로 검색된 결과가 없는 경우 빈 page 객체를 반환한다.")
	@Test
	void searchByAuthorAndKeywordNoResult() {
		//given
		Member member1 = createMember("우경서");
		Post post1 = createPost("제목1", "내용1", member1);
		Post post2 = createPost("제목1", "내용2", member1);
		Post post3 = createPost("제목2", "내용3", member1);

		Member member2 = createMember("김경서");
		Post post4 = createPost("제목4", "내용4", member2);

		memberRepository.saveAll(List.of(member1, member2));
		postRepository.saveAll(List.of(post1, post2, post3, post4));

		PostSearchCondition searchCondition = PostSearchCondition.builder()
			.author("박경서")
			.keyword("5")
			.build();

		Pageable pageRequest = PageRequest.of(0, 3);

		//when
		Page<PostResponseDto> posts = postRepository.search(searchCondition, pageRequest);

		//then
		assertThat(posts).hasSize(0);
		assertThat(posts.getContent()).isEmpty();

	}

	private Member createMember(String name) {
		return Member.builder()
			.name(name)
			.build();
	}

	private Post createPost(String title, String content, Member member) {
		return Post.builder()
			.title(title)
			.content(content)
			.member(member)
			.build();
	}

}