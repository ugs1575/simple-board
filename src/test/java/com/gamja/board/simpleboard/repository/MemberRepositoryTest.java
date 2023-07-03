package com.gamja.board.simpleboard.repository;

import static org.assertj.core.groups.Tuple.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.IntegrationTestSupport;
import com.gamja.board.simpleboard.entity.Member;

@Transactional
class MemberRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@DisplayName("작성자 명으로 회원 정보를 찾는다.")
	@Test
	void findByNameContaining() {
	    //given
		Member member1 = createMember("우경서");
		Member member2 = createMember("김경서");
		Member member3 = createMember("이경서");

		memberRepository.saveAll(List.of(member1, member2, member3));

		//when
		Page<Member> members = memberRepository.findByNameContaining("우", PageRequest.of(0, 2));

		//then
		Assertions.assertThat(members).hasSize(1)
			.extracting("name")
			.containsExactlyInAnyOrder(
				tuple("우경서")
			);
	}

	private Member createMember(String name) {
		return Member.builder()
			.name(name)
			.build();
	}

}