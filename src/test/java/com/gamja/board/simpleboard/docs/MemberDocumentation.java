package com.gamja.board.simpleboard.docs;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDocumentation {
	public static RestDocumentationResultHandler createMember() {
		return document("member/create",
			preprocessRequest(prettyPrint()),
			requestFields(
				fieldWithPath("name").type(JsonFieldType.STRING).description("회원명")
			),
			responseHeaders(
				headerWithName("Location").description("생성된 회원 id")
			));
	}

	public static RestDocumentationResultHandler updateMember() {
		return document("member/update",
			preprocessRequest(prettyPrint()),
			pathParameters(
				parameterWithName("memberId").description("회원 아이디")
			),
			requestFields(
				fieldWithPath("name").type(JsonFieldType.STRING).description("회원명")
			));
	}

	public static RestDocumentationResultHandler findMember() {
		return document("member/find",
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("memberId").description("회원 아이디")
			),
			responseFields(
				fieldWithPath("id").type(JsonFieldType.NUMBER)
					.description("회원 ID"),
				fieldWithPath("name").type(JsonFieldType.STRING)
					.description("회원 이름")
			));
	}

	public static RestDocumentationResultHandler findMembers() {
		return document("member/findAll",
			preprocessResponse(prettyPrint()),
			requestParameters(
				parameterWithName("name").description("회원명"),
				parameterWithName("page").description("페이지 번호"),
				parameterWithName("size").description("한 페이지 당 데이터 수")
			),
			responseFields(
				fieldWithPath("count").type(JsonFieldType.NUMBER)
					.description("총 데이터 수"),
				fieldWithPath("data").type(JsonFieldType.ARRAY)
					.description("응답 데이터"),
				fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
					.description("회원 ID"),
				fieldWithPath("data.[].name").type(JsonFieldType.STRING)
					.description("회원 이름")
			));
	}

	public static RestDocumentationResultHandler deleteMember() {
		return document("member/delete",
			pathParameters(
				parameterWithName("memberId").description("회원 아이디")
			));
	}
}
