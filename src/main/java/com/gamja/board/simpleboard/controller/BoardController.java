package com.gamja.board.simpleboard.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gamja.board.simpleboard.dto.PostForm;
import com.gamja.board.simpleboard.dto.PostResponseDto;
import com.gamja.board.simpleboard.service.PostService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

	private final PostService postService;

	@GetMapping("/board/list")
	public String list(Model model, Pageable pageable) {
		List<PostResponseDto> posts = postService.findPosts(pageable);
		model.addAttribute("posts", posts);
		return "board/listForm";
	}

	@GetMapping("/board/new")
	public String createForm(Model model, @RequestParam(required = false) Long id) {
		model.addAttribute("postForm", new PostForm());
		return "board/createForm";
	}

	@PostMapping("/board/new")
	public String create(@Valid PostForm postForm, BindingResult result) {

		if (result.hasErrors()) {
			return "board/createForm";
		}

		postService.save(1L, postForm);
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
	public String update(Model model, @PathVariable Long boardId, @Valid PostForm postForm, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("postForm", postForm);
			return "board/updateForm";
		}

		postService.update(boardId, postForm);
		return "redirect:/board/" + boardId;
	}
}
