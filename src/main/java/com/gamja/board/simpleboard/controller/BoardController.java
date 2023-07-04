package com.gamja.board.simpleboard.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gamja.board.simpleboard.dto.PostSaveForm;
import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.dto.PostSearchCondition;
import com.gamja.board.simpleboard.dto.PostUpdateForm;
import com.gamja.board.simpleboard.service.PostService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

	private final PostService postService;

	@GetMapping("/board/list")
	public String list(Model model, PostSearchCondition condition, @PageableDefault(size = 2) Pageable pageable) {
		Page<PostResponseDto> posts = postService.getBoard(condition, pageable);

		int totalPage = 5;
		int startPage = (posts.getPageable().getPageNumber() / totalPage) * totalPage + 1;
		int endPage = Math.min(posts.getTotalPages(), startPage + totalPage - 1);

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("posts", posts);
		return "board/listForm";
	}

	@GetMapping("/board/new")
	public String createForm(Model model, @RequestParam(required = false) Long id) {
		model.addAttribute("postForm", new PostSaveForm());
		return "board/createForm";
	}

	@PostMapping("/board/new")
	public String create(@Valid PostSaveForm postSaveForm, BindingResult result) {

		if (result.hasErrors()) {
			return "board/createForm";
		}

		postService.save(1L, postSaveForm.toServiceRequest());
		return "redirect:/board/list";
	}

	@GetMapping("/board/{boardId}")
	public String findBoard(Model model, @PathVariable Long boardId) {
		PostResponseDto post = postService.findById(boardId);
		model.addAttribute("postForm", post);
		return "board/detailForm";
	}

	@GetMapping("/board/{boardId}/edit")
	public String update(Model model, @PathVariable Long boardId) {
		PostResponseDto post = postService.findById(boardId);
		model.addAttribute("postForm", post);
		return "board/updateForm";
	}

	@PostMapping("/board/{boardId}/edit")
	public String update(Model model, @PathVariable Long boardId, @Valid PostUpdateForm postUpdateForm, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("postForm", postUpdateForm);
			return "board/updateForm";
		}

		//todo: postform save, update 구분
		postService.update(1L, boardId, postUpdateForm.toServiceRequest());
		return "redirect:/board/" + boardId;
	}
}
