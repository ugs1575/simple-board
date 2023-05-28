package com.gamja.board.simpleboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamja.board.simpleboard.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
