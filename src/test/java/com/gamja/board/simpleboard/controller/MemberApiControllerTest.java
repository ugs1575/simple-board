package com.gamja.board.simpleboard.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.dto.MemberUpdateRequestDto;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberApiControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	public void afterEach() {
		memberRepository.deleteAll();
	}


	@Test
	public void 회원이_등록된다() throws Exception {
	    //given
		String name = "우경서";

		MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
															.name(name)
															.build();

		String url = "http://localhost:" + port + "/api/members";

	    //when
		ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

	    //then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isGreaterThan(0L);

		List<Member> all = memberRepository.findAll();
		assertThat(all.get(0).getName()).isEqualTo(name);
	}

	@Test
	public void 회원_정보를_수정한다() throws Exception {
		//given
		Member saveMember = memberRepository.save(Member.builder()
													.name("우경서")
													.build());

		Long id = saveMember.getId();
		String expectedName = "우경서2";

		MemberUpdateRequestDto requestDto = MemberUpdateRequestDto.builder()
																.name(expectedName)
																.build();

		String url = "http://localhost:" + port + "/api/members/" + id;

		HttpEntity<MemberUpdateRequestDto> requestHttpEntity = new HttpEntity<>(requestDto);

		//when
		ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestHttpEntity, Long.class);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo(id);

		List<Member> all = memberRepository.findAll();
		assertThat(all.get(0).getName()).isEqualTo(expectedName);
	}



}