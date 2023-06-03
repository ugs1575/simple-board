package com.gamja.board.simpleboard.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gamja.board.simpleboard.dto.PostSaveRequestDto;
import com.gamja.board.simpleboard.dto.PostUpdateRequestDto;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.entity.Post;
import com.gamja.board.simpleboard.repository.MemberRepository;
import com.gamja.board.simpleboard.repository.PostRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	private Member member;

	@BeforeEach
	public void setUp() {
		member = memberRepository.save(Member.builder()
			.name("우경서")
			.build());
	}

	@AfterEach
	public void afterEach() {
		postRepository.deleteAll();
		memberRepository.deleteAll();
	}


	@Test
	public void 게시글을_작성한다() throws Exception {
	    //given
		String expectedTitle = "제목1";
		String expectedContent = "테스트";

		PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
															.title(expectedTitle)
															.content(expectedContent)
															.build();

		String url = "http://localhost:" + port + "/api/members/" + member.getId() + "/posts";

	    //when
		ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

	    //then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isGreaterThan(0L);

		List<Post> all = postRepository.findAll();
		assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
		assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
		assertThat(all.get(0).getMember().getId()).isEqualTo(member.getId());
	}

	@Test
	public void 게시글을_수정한다() throws Exception {
		//given
		Post post = postRepository.save(Post.builder()
			.title("제목1")
			.content("글내용1")
			.member(member)
			.build());

		String expectedTitle = "제목1_수정";
		String expectedContent = "글내용1_수정";

		PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
			.title(expectedTitle)
			.content(expectedContent)
			.build();

		String url = "http://localhost:" + port + "/api/members/" + member.getId() + "/posts/" + post.getId();

		HttpEntity<PostUpdateRequestDto> requestHttpEntity = new HttpEntity<>(requestDto);

		//when
		ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestHttpEntity, Long.class);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo(post.getId());

		List<Post> all = postRepository.findAll();
		assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
		assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
	}





}