package com.gamja.board.simpleboard.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import com.gamja.board.simpleboard.ControllerTestSupport;
import com.gamja.board.simpleboard.dto.MemberResponseDto;
import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.dto.MemberSaveServiceRequest;
import com.gamja.board.simpleboard.dto.MemberUpdateRequestDto;
import com.gamja.board.simpleboard.dto.MemberUpdateServiceRequest;

class MemberApiControllerTest extends ControllerTestSupport {

	@DisplayName("신규 회원을 등록한다.")
	@Test
	void createMember() throws Exception {
	    //given
		MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
			.name("우경서")
			.build();

		given(memberService.save(any(MemberSaveServiceRequest.class))).willReturn(1L);

	    //when //then
		mockMvc.perform(
				post("/api/members")
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/members/1"));
	}

	@DisplayName("신규 회원 등록 시 이름은 필수 값이다.")
	@Test
	void createMemberWithoutName() throws Exception {
		//given
		MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
			.build();

		//when //then
		mockMvc.perform(
				post("/api/members")
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.statusName").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
			.andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("회원 이름은 필수 입니다."))
		;

	}

	@DisplayName("회원정보를 수정한다.")
	@Test
	void updateMember() throws Exception {
		//given
		MemberUpdateRequestDto requestDto = MemberUpdateRequestDto.builder()
			.name("우경서_수정")
			.build();

		//when //then
		mockMvc.perform(
				patch("/api/members/{id}", 1L)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@DisplayName("회원 정보 수정 시 이름은 필수 값이다.")
	@Test
	void updateMemberWithoutName() throws Exception {
		//given
		MemberUpdateRequestDto requestDto = MemberUpdateRequestDto.builder()
			.build();

		//when //then
		mockMvc.perform(
				patch("/api/members/{id}", 1L)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.statusName").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
			.andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("회원 이름은 필수 입니다."))
		;
	}

	@DisplayName("회원정보를 조회한다.")
	@Test
	void findMember() throws Exception {
		//given
		given(memberService.findById(anyLong()))
			.willReturn(MemberResponseDto.builder()
				.id(1L)
				.name("우경서")
				.build()
			);

		//when //then
		mockMvc.perform(
				get("/api/members/{id}", 1L)
					.accept(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.name").value("우경서"));
	}

	@DisplayName("회원정보를 모두 조회한다.")
	@Test
	void findMembers() throws Exception {
		//given
		MemberResponseDto responseDto1 = MemberResponseDto.builder()
			.id(1L)
			.name("우경서")
			.build();

		MemberResponseDto responseDto2 = MemberResponseDto.builder()
			.id(2L)
			.name("우경서2")
			.build();

		given(memberService.findMembers(anyString(), any(Pageable.class)))
			.willReturn(List.of(responseDto1, responseDto2));

		//when //then
		mockMvc.perform(
				get("/api/members")
					.accept(MediaType.APPLICATION_JSON)
					.param("size", "3")
					.param("name", (String)null)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count").value(2))
			.andExpect(jsonPath("$.data[0].id").value(1L))
			.andExpect(jsonPath("$.data[0].name").value("우경서"))
			.andExpect(jsonPath("$.data[1].id").value(2L))
			.andExpect(jsonPath("$.data[1].name").value("우경서2"));
	}
	
	@DisplayName("회원 정보를 삭제한다.")
	@Test
	void deleteMember() throws Exception {
	    //given
		doNothing().when(memberService).delete(anyLong());

	    //when //then
		mockMvc.perform(
				delete("/api/members/{id}", 1L)
			)
			.andDo(print())
			.andExpect(status().isNoContent());
	}
}