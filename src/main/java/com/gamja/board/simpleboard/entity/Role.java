package com.gamja.board.simpleboard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.gamja.board.simpleboard.common.domain.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Role extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "role_id")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Builder
	public Role(String name) {
		this.name = name;
	}

}
