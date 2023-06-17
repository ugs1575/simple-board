package com.gamja.board.simpleboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.dto.PostSearchCondition;

public interface PostRepositoryCustom {
	Page<PostResponseDto> search(PostSearchCondition condition, Pageable pageable);
}
