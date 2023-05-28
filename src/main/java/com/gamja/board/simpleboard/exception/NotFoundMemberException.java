package com.gamja.board.simpleboard.exception;

public class NotFoundMemberException extends NotFoundException {
	private static final String ERROR_MESSAGE = "존재하지 않는 회원입니다.";

	public NotFoundMemberException() {
		super(ERROR_MESSAGE);
	}
}