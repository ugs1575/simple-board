package com.gamja.board.simpleboard.common.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import com.gamja.board.simpleboard.exception.ErrorCode;

import lombok.Getter;

@Getter
public class ErrorResponse {
	private final LocalDateTime timestamp = LocalDateTime.now();
	private final int status;
	private final String statusName;
	private final String code;
	private final String message;
	private final List<FieldError> fieldErrors;

	private ErrorResponse(ErrorCode errorCode, List<FieldError> fieldErrors) {
		this.status = errorCode.getHttpStatus().value();
		this.statusName = errorCode.getHttpStatus().name();
		this.code = errorCode.name();
		this.message = errorCode.getMessage();
		this.fieldErrors = fieldErrors;
	}

	private ErrorResponse(ErrorCode errorCode) {
		this.status = errorCode.getHttpStatus().value();
		this.statusName = errorCode.getHttpStatus().name();
		this.code = errorCode.name();
		this.message = errorCode.getMessage();
		this.fieldErrors = new ArrayList<>();
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode);

	}

	public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
		return new ErrorResponse(errorCode, FieldError.of(bindingResult));
	}

	@Getter
	static class FieldError {
		private String field;
		private String message;

		private FieldError(String field, String message) {
			this.field = field;
			this.message = message;
		}

		private static List<FieldError> of(BindingResult bindingResult) {
			return bindingResult.getFieldErrors()
				.stream()
				.map(statusName -> new FieldError(
					statusName.getField(),
					statusName.getDefaultMessage())
				)
				.collect(Collectors.toList());
		}
	}
}
