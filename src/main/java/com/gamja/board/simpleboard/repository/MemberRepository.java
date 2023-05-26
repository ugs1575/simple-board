package com.gamja.board.simpleboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamja.board.simpleboard.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
