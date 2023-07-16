package com.gamja.board.simpleboard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.gamja.board.simpleboard.ControllerTestSupport;
import com.gamja.board.simpleboard.dto.PostSaveServiceRequest;
import com.gamja.board.simpleboard.dto.PostUpdateServiceRequest;
import com.gamja.board.simpleboard.service.PostService;

class PostApiControllerTest extends ControllerTestSupport {

	@MockBean
	protected PostService postService;

	@DisplayName("신규 게시글 등록 시 제목은 필수 값입니다.")
	@Test
	void createPostWithoutTitle() throws Exception {
		//given
		PostSaveServiceRequest requestDto = PostSaveServiceRequest.builder()
			.content("내용1")
			.build();

		//when //then
		mockMvc.perform(
				post("/api/members/{memberId}/posts", 1L)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.statusName").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
			.andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("title"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("제목은 필수 입니다."))
			.andDo(print());

	}

	@DisplayName("게시글 수정 시 제목은 필수 값이다.")
	@Test
	void updatePostWithoutTitle() throws Exception {
		//given
		PostUpdateServiceRequest requestDto = PostUpdateServiceRequest.builder()
			.content("내용1")
			.build();

		//when //then
		mockMvc.perform(
				patch("/api/members/{memberId}/posts/{postId}", 1L, 1L)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.statusName").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
			.andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("title"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("제목은 필수 입니다."))
			.andDo(print());
	}
}