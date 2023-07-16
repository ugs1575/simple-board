package com.gamja.board.simpleboard.docs;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.anyLong;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.gamja.board.simpleboard.RestDocsSupport;
import com.gamja.board.simpleboard.controller.PostApiController;
import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.dto.PostSaveServiceRequest;
import com.gamja.board.simpleboard.dto.PostUpdateServiceRequest;
import com.gamja.board.simpleboard.service.PostService;

class PostApiControllerDocsTest extends RestDocsSupport {

	private final PostService postService = mock(PostService.class);

	@Override
	protected Object initController() {
		return new PostApiController(postService);
	}

	@DisplayName("게시글을 작성한다.")
	@Test
	void createPost() throws Exception {
		PostSaveServiceRequest requestDto = PostSaveServiceRequest.builder()
			.title("제목1")
			.content("내용1")
			.build();

		given(postService.save(anyLong(), any(PostSaveServiceRequest.class), any(LocalDateTime.class))).willReturn(1L);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/members/{memberId}/posts", 1L)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/posts/1"))
			.andDo(print())
			.andDo(PostDocumentation.createPost());
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
				RestDocumentationRequestBuilders.patch("/api/members/{memberId}/posts/{postId}", 1L, 1L)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(PostDocumentation.updatePost());
	}

	@DisplayName("게시글을 조회한다.")
	@Test
	void findPost() throws Exception {
		PostResponseDto responseDto = PostResponseDto.builder()
			.postId(1L)
			.title("제목")
			.content("내용")
			.registeredDateTime(LocalDateTime.now())
			.memberId(1L)
			.memberName("우경서")
			.build();

		given(postService.findById(anyLong())).willReturn(responseDto);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/posts/{postId}", 1L)
					.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(PostDocumentation.findPost());
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
					.param("page", "0")
					.param("size", "3")
					.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count").value(2))
			.andExpect(jsonPath("$.data").isArray())
			.andDo(print())
			.andDo(PostDocumentation.findPosts());
	}

	@DisplayName("게시글을 삭제한다.")
	@Test
	void deletePost() throws Exception {
		//given
		doNothing().when(postService).delete(anyLong(), anyLong());

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/members/{memberId}/posts/{postId}", 1L, 1L)
			)
			.andExpect(status().isNoContent())
			.andDo(print())
			.andDo(PostDocumentation.deletePost());
	}
}