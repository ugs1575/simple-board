package com.gamja.board.simpleboard.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class MemberApiControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MemberRepository memberRepository;

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

}