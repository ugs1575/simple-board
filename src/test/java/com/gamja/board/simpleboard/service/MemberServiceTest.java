package com.gamja.board.simpleboard.service;

import static com.gamja.board.simpleboard.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.gamja.board.simpleboard.IntegrationTestSupport;
import com.gamja.board.simpleboard.dto.MemberResponseDto;
import com.gamja.board.simpleboard.dto.MemberSaveServiceRequest;
import com.gamja.board.simpleboard.dto.MemberUpdateServiceRequest;
import com.gamja.board.simpleboard.entity.Member;
import com.gamja.board.simpleboard.exception.CustomException;
import com.gamja.board.simpleboard.repository.MemberRepository;

class MemberServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void tearDown() {
		memberRepository.deleteAllInBatch();
	}

	@DisplayName("회원정보가 저장 후 회원 아이디가 반환된다.")
	@Test
	void createMember() {
	    //given
		MemberSaveServiceRequest memberRequest = MemberSaveServiceRequest.builder()
			.name("우경서")
			.build();

		//when
		Long memberId = memberService.save(memberRequest);

		//then
		assertThat(memberId).isGreaterThan(0L);

		Optional<Member> member = memberRepository.findById(memberId);
		assertThat(member.isPresent()).isTrue();
		assertThat(member.get().getName()).isEqualTo("우경서");
	}

	@DisplayName("회원 정보를 수정하면 수정된 정보가 저장된다.")
	@Test
	void updateMember() {
		//given
		Member member = createMember("우경서");
		memberRepository.save(member);

		MemberUpdateServiceRequest memberRequest = MemberUpdateServiceRequest.builder()
			.name("김경서")
			.build();

		//when
		Long memberId = memberService.update(member.getId(), memberRequest);

		//then
		assertThat(memberId).isEqualTo(member.getId());

		Optional<Member> updateMember = memberRepository.findById(memberId);
		assertThat(updateMember.isPresent()).isTrue();
		assertThat(updateMember.get().getName()).isEqualTo("김경서");
	}

	@DisplayName("존재하지 않는 회원은 회원 정보를 수정할 수 없다.")
	@Test
	void updateNotExistedMember() {
		//given
		MemberUpdateServiceRequest memberRequest = MemberUpdateServiceRequest.builder()
			.name("김경서")
			.build();

		//when, then
		assertThatThrownBy(() -> memberService.update(1L, memberRequest))
			.isInstanceOf(CustomException.class)
			.hasMessage(MEMBER_NOT_FOUND.getMessage());
	}
	
	@DisplayName("회원 아이디로 회원 정보를 조회한다.")
	@Test
	void findMemberById() {
		//given
		Member member = createMember("우경서");
		memberRepository.save(member);

		MemberResponseDto expected = MemberResponseDto.of(member);

		//when
		MemberResponseDto actual = memberService.findById(member.getId());

		//then
		assertThat(actual.getId()).isEqualTo(expected.getId());
	}

	@DisplayName("존재하지 않는 회원을 조회할 경우 예외가 발생한다.")
	@Test
	void findNotExistedMember() {
		//when, then
		assertThatThrownBy(() -> memberService.findById(1L))
			.isInstanceOf(CustomException.class)
			.hasMessage(MEMBER_NOT_FOUND.getMessage());
	}
	
	@DisplayName("회원이름으로 검색조건에 맞는 회원 목록을 조회한다.")
	@Test
	void findMembersByName() {
		//given
		Member member1 = createMember("우경서");
		Member member2 = createMember("김우경서");
		Member member3 = createMember("이경서");

		memberRepository.saveAll(List.of(member1, member2, member3));

		PageRequest pageRequest = PageRequest.of(0, 3);

		//when
		List<MemberResponseDto> members = memberService.findMembers("우", pageRequest);

		//then
		assertThat(members).hasSize(2)
			.extracting("name")
			.containsExactlyInAnyOrder(
				"우경서", "김우경서"
			);
	}

	@DisplayName("회원이름으로 검색조건에 맞는 회원이 없는 경우 빈 객체를 반환한다.")
	@Test
	void findMembersByNameNoResult() {
		//given
		Member member1 = createMember("우경서");
		Member member2 = createMember("김우경서");
		Member member3 = createMember("이경서");

		memberRepository.saveAll(List.of(member1, member2, member3));

		PageRequest pageRequest = PageRequest.of(0, 3);

		//when
		List<MemberResponseDto> members = memberService.findMembers("박", pageRequest);

		//then
		assertThat(members).hasSize(0)
			.isEmpty();
	}
	
	@DisplayName("회원 정보를 삭제 후 조회할 경우 빈객체를 반환한다.")
	@Test
	void deleteMember() {
		//given
		Member member = createMember("우경서");
		memberRepository.save(member);

		//when
		memberService.delete(member.getId());

		//then
		Optional<Member> byId = memberRepository.findById(member.getId());
		assertThat(byId).isEmpty();
	}

	@DisplayName("존재하지 않는 회원은 삭제 시도 시 예외가 발생한다.")
	@Test
	void deleteNotExistedMember() {
		//when, then
		assertThatThrownBy(() -> memberService.delete(1L))
			.isInstanceOf(CustomException.class)
			.hasMessage(MEMBER_NOT_FOUND.getMessage());
	}

	private Member createMember(String name) {
		return Member.builder()
			.name(name)
			.build();
	}

}