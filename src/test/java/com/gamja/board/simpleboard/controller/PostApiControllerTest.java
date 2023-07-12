package com.gamja.board.simpleboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import com.gamja.board.simpleboard.ControllerTestSupport;
import com.gamja.board.simpleboard.dto.MemberUpdateRequestDto;
import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.dto.PostSaveServiceRequest;
import com.gamja.board.simpleboard.dto.PostUpdateServiceRequest;

class PostApiControllerTest extends ControllerTestSupport {

	@DisplayName("글을 작성한다.")
	@Test
	void createPost() throws Exception {
		//given
		PostSaveServiceRequest requestDto = PostSaveServiceRequest.builder()
			.title("제목1")
			.content("내용1")
			.build();

		given(postService.save(anyLong(), any(PostSaveServiceRequest.class), any(LocalDateTime.class))).willReturn(1L);

		//when //then
		mockMvc.perform(
				post("/api/members/{memberId}/posts", 1L)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/posts/1"));
	}

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
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.statusName").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
			.andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("title"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("제목은 필수 입니다."))
		;

	}

	@DisplayName("게시글을 수정한다.")
	@Test
	void updatePost() throws Exception {
		//given
		PostUpdateServiceRequest requestDto = PostUpdateServiceRequest.builder()
			.title("제목_수정")
			.content("내용_수정")
			.build();

		//when //then
		mockMvc.perform(
				patch("/api/members/{memberId}/posts/{postId}", 1L, 1L)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
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
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.statusName").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
			.andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("title"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("제목은 필수 입니다."));
	}

	@DisplayName("회원정보를 조회한다.")
	@Test
	void findPost() throws Exception {
		mockMvc.perform(
				get("/api/posts/{postId}", 1L)
					.accept(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@DisplayName("모든 게시물을 조회한다.")
	@Test
	void findPosts() throws Exception {
		//given
		LocalDateTime now = LocalDateTime.now();

		PostResponseDto responseDto1 = PostResponseDto.builder()
			.postId(1L)
			.title("제목")
			.content("내용")
			.registeredDateTime(now)
			.memberId(1L)
			.memberName("우경서")
			.build();


		PostResponseDto responseDto2 = PostResponseDto.builder()
			.postId(2L)
			.title("제목2")
			.content("내용2")
			.registeredDateTime(now)
			.memberId(1L)
			.memberName("우경서")
			.build();

		given(postService.findPosts(any(Pageable.class)))
			.willReturn(List.of(responseDto1, responseDto2));

		//when //then
		mockMvc.perform(
				get("/api/posts")
					.param("size", "3")
					.accept(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count").value(2))
			.andExpect(jsonPath("$.data").isArray());
	}

	@DisplayName("게시글을 삭제한다.")
	@Test
	void deletePost() throws Exception {
		//given
		doNothing().when(postService).delete(anyLong(), anyLong());

		//when //then
		mockMvc.perform(
				delete("/api/members/{memberId}/posts/{postId}", 1L, 1L)
			)
			.andDo(print())
			.andExpect(status().isNoContent());
	}
}