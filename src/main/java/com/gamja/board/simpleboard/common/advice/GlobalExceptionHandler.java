package com.gamja.board.simpleboard.common.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gamja.board.simpleboard.common.dto.ErrorResponse;
import com.gamja.board.simpleboard.exception.CustomException;
import com.gamja.board.simpleboard.exception.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
		ErrorResponse response = ErrorResponse.of(errorCode, e.getBindingResult());
		return new ResponseEntity<>(response, errorCode.getHttpStatus());
	}

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		ErrorResponse response = ErrorResponse.of(errorCode);
		return new ResponseEntity<>(response, errorCode.getHttpStatus());
	}
}