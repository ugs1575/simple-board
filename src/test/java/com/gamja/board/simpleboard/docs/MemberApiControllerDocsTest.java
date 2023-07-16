package com.gamja.board.simpleboard.docs;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.gamja.board.simpleboard.RestDocsSupport;
import com.gamja.board.simpleboard.controller.MemberApiController;
import com.gamja.board.simpleboard.dto.MemberResponseDto;
import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.dto.MemberSaveServiceRequest;
import com.gamja.board.simpleboard.dto.MemberUpdateRequestDto;
import com.gamja.board.simpleboard.service.MemberService;

@WebMvcTest(controllers = MemberApiController.class)
public class MemberApiControllerDocsTest extends RestDocsSupport {

	@MockBean
	protected MemberService memberService;

	@DisplayName("신규 회원을 등록한다.")
	@Test
	void createMember() throws Exception {
		MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
			.name("우경서")
			.build();

		given(memberService.save(any(MemberSaveServiceRequest.class))).willReturn(1L);

		mockMvc.perform(
				post("/api/members")
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/members/1"))
			.andDo(print())
			.andDo(MemberDocumentation.createMember());
	}

	@DisplayName("회원정보를 수정한다.")
	@Test
	void updateMember() throws Exception {
		MemberUpdateRequestDto requestDto = MemberUpdateRequestDto.builder()
			.name("우경서_수정")
			.build();

		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/members/{memberId}", 1L)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberDocumentation.updateMember());
	}

	@DisplayName("회원정보를 조회한다.")
	@Test
	void findMember() throws Exception {
		MemberResponseDto responseDto1 = MemberResponseDto.builder()
			.id(1L)
			.name("jane")
			.build();

		given(memberService.findById(anyLong()))
			.willReturn(responseDto1);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/members/{memberId}", 1L)
					.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberDocumentation.findMember());
	}

	@DisplayName("회원정보를 모두 조회한다.")
	@Test
	void findMembers() throws Exception {
		MemberResponseDto responseDto1 = MemberResponseDto.builder()
			.id(1L)
			.name("jane")
			.build();

		MemberResponseDto responseDto2 = MemberResponseDto.builder()
			.id(2L)
			.name("jenny")
			.build();

		given(memberService.findMembers(anyString(), any(Pageable.class)))
			.willReturn(List.of(responseDto1, responseDto2));

		mockMvc.perform(
				get("/api/members")
					.accept(MediaType.APPLICATION_JSON)
					.param("page", "0")
					.param("size", "3")
					.param("name", "j")
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count").value(2))
			.andExpect(jsonPath("$.data").isArray())
			.andDo(print())
			.andDo(MemberDocumentation.findMembers());
	}

	@DisplayName("회원 정보를 삭제한다.")
	@Test
	void deleteMember() throws Exception {
		doNothing().when(memberService).delete(anyLong());

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/members/{memberId}", 1L)
			)
			.andExpect(status().isNoContent())
			.andDo(print())
			.andDo(MemberDocumentation.deleteMember());
	}
}
