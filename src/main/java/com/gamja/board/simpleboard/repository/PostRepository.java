package com.gamja.board.simpleboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gamja.board.simpleboard.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Query("select p from Post p join fetch p.member where p.id = :id")
	Optional<Post> findByIdFetchJoin(@Param("id") Long id);
}
