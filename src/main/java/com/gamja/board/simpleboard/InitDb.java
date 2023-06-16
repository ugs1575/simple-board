package com.gamja.board.simpleboard;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gamja.board.simpleboard.entity.Role;
import com.gamja.board.simpleboard.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() {
		initService.createUserRole();
		initService.createAdminRole();
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {

		private static final String ROLE_ADMIN = "ROLE_ADMIN";
		private static final String ROLE_USER = "ROLE_USER";
		private final RoleRepository roleRepository;

		public void createUserRole() {
			Role role = Role.builder()
				.name(ROLE_USER)
				.build();

			roleRepository.save(role);
		}

		public void createAdminRole() {
			Role role = Role.builder()
				.name(ROLE_ADMIN)
				.build();

			roleRepository.save(role);
		}
	}
}

