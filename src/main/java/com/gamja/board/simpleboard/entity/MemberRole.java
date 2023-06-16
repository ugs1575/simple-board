package com.gamja.board.simpleboard.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gamja.board.simpleboard.common.domain.BaseTimeEntity;

@Entity
public class MemberRole extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	public static void createMemberUserRole(Member member) {

	}
}
