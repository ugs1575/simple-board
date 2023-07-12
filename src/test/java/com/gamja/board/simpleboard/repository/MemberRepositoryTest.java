package com.gamja.board.simpleboard.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
		Member member2 = createMember("김우경서");
		Member member3 = createMember("이경서");

		memberRepository.saveAll(List.of(member1, member2, member3));

		PageRequest pageRequest = PageRequest.of(0, 3);

		//when
		Page<Member> members = memberRepository.findByNameContainingIgnoreCase("우", pageRequest);

		//then
		assertThat(members).hasSize(2)
			.extracting("name")
			.containsExactlyInAnyOrder(
				"우경서", "김우경서"
			);
	}

	@DisplayName("검색된 결과가 없는 경우 빈 page 객체를 반환한다.")
	@Test
	void findByNameContainingNoResult() {
		//given
		Member member1 = createMember("우경서");
		Member member2 = createMember("김우경서");
		Member member3 = createMember("이경서");

		memberRepository.saveAll(List.of(member1, member2, member3));

		PageRequest pageRequest = PageRequest.of(0, 3);

		//when
		Page<Member> members = memberRepository.findByNameContainingIgnoreCase("차", pageRequest);

		//then
		assertThat(members).hasSize(0);
		assertThat(members.getContent()).isEmpty();
	}

	private Member createMember(String name) {
		return Member.builder()
			.name(name)
			.build();
	}

}