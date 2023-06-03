package com.gamja.board.simpleboard.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
	POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다."),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
	POST_UNAUTHORIZED(HttpStatus.FORBIDDEN, "해당글의 작성자가 아닙니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
