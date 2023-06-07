package com.gamja.board.simpleboard.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gamja.board.simpleboard.dto.PostForm;
import com.gamja.board.simpleboard.entity.Post;
import com.gamja.board.simpleboard.service.PostService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {

	private final PostService postService;

	@GetMapping("/list")
	public String list(Model model) {
		List<Post> posts = postService.findPosts();
		model.addAttribute("posts", posts);
		return "board/list";
	}

	@GetMapping("/posts/new")
	public String createForm(Model model) {
		model.addAttribute("postForm", new PostForm());
		return "board/form";
	}

	@PostMapping("/posts/new")
	public String create(@Valid PostForm postForm, BindingResult result) {

		if (result.hasErrors()) {
			return "board/form";
		}

		postService.save(1L, postForm);
		return "redirect:/";
	}
}
