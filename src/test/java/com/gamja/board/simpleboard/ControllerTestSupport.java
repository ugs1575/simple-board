package com.gamja.board.simpleboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamja.board.simpleboard.controller.MemberApiController;
import com.gamja.board.simpleboard.controller.PostApiController;
import com.gamja.board.simpleboard.service.MemberService;
import com.gamja.board.simpleboard.service.PostService;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
	MemberApiController.class,
	PostApiController.class
})
public abstract class ControllerTestSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;
}
