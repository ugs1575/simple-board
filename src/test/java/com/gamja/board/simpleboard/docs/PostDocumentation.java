package com.gamja.board.simpleboard.docs;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.time.LocalDateTime;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class PostDocumentation {
	public static RestDocumentationResultHandler createPost() {
		return document("post/create",
			preprocessRequest(prettyPrint()),
			pathParameters(
				parameterWithName("memberId").description("작성자 아이디")
			),
			requestFields(
				fieldWithPath("title").type(JsonFieldType.STRING)
					.description("게시글 제목"),
				fieldWithPath("content").type(JsonFieldType.STRING)
					.optional()
					.description("게시글 내용")
			),
			responseHeaders(
				headerWithName("Location").description("생성된 게시글 id")
			));
	}

	public static RestDocumentationResultHandler updatePost() {
		return document("post/update",
			preprocessRequest(prettyPrint()),
			pathParameters(
				parameterWithName("memberId").description("수정하는 회원 아이디"),
				parameterWithName("postId").description("게시글 아이디")
			),
			requestFields(
				fieldWithPath("title").type(JsonFieldType.STRING)
					.description("게시글 제목"),
				fieldWithPath("content").type(JsonFieldType.STRING)
					.optional()
					.description("게시글 내용")
			));
	}

	public static RestDocumentationResultHandler findPost() {
		return document("post/find",
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("postId").description("게시글 아이디")
			),
			responseFields(
				fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 ID"),
				fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
				fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
				fieldWithPath("registeredDateTime").type(JsonFieldType.ARRAY).description("게시글 등록일"),
				fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 ID"),
				fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자명")
			));
	}

	public static RestDocumentationResultHandler findPosts() {
		return document("post/findAll",
			preprocessResponse(prettyPrint()),
			requestParameters(
				parameterWithName("page").description("페이지 번호"),
				parameterWithName("size").description("한 페이지 당 데이터 수")
			),
			responseFields(
				fieldWithPath("count").type(JsonFieldType.NUMBER)
					.description("총 데이터 수"),
				fieldWithPath("data").type(JsonFieldType.ARRAY)
					.description("응답 데이터"),
				fieldWithPath("data.[].postId").type(JsonFieldType.NUMBER)
					.description("게시글 ID"),
				fieldWithPath("data.[].title").type(JsonFieldType.STRING)
					.description("게시글 제목"),
				fieldWithPath("data.[].content").type(JsonFieldType.STRING)
					.description("게시글 내용"),
				fieldWithPath("data.[].registeredDateTime").type(JsonFieldType.ARRAY)
					.description("게시글 등록일"),
				fieldWithPath("data.[].memberId").type(JsonFieldType.NUMBER)
					.description("작성자 ID"),
				fieldWithPath("data.[].memberName").type(JsonFieldType.STRING)
					.description("작성자명")
			));
	}

	public static RestDocumentationResultHandler deletePost() {
		return document("post/delete",
			pathParameters(
				parameterWithName("memberId").description("삭제하는 회원 아이디"),
				parameterWithName("postId").description("게시글 아이디")
			));
	}
}
