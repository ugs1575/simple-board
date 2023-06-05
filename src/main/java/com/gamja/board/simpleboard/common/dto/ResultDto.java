package com.gamja.board.simpleboard.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultDto<T> {
	private int count;
	private T data;

	public static<T> ResultDto<T> of(int count, T data) {
		return ResultDto.<T>builder()
			.count(count)
			.data(data)
			.build();
	}
}
