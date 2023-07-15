package com.gamja.board.simpleboard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.gamja.board.simpleboard.ControllerTestSupport;
import com.gamja.board.simpleboard.dto.MemberSaveRequestDto;
import com.gamja.board.simpleboard.dto.MemberUpdateRequestDto;

class MemberApiControllerTest extends ControllerTestSupport {

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
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.statusName").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
			.andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("회원 이름은 필수 입니다."))
			.andDo(print());

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
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.statusName").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
			.andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("회원 이름은 필수 입니다."))
			.andDo(print());
	}
}