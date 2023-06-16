package com.gamja.board.simpleboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamja.board.simpleboard.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
