package com.gamja.board.simpleboard.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.gamja.board.simpleboard.common.domain.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;

	@Column(length = 50, nullable = false)
	private String account;

	@Column(length = 100, nullable = false)
	private String password;

	@Column(length = 100, nullable = false, columnDefinition = "boolean default true")
	private boolean enabled;

	@OneToMany(mappedBy = "member")
	private List<MemberRole> MemberRoles = new ArrayList<>();

	@Builder
	public Member(String name, String account, String password) {
		this.name = name;
		this.account = account;
		this.password = password;
	}

	public void update(String name) {
		this.name = name;
	}

	public void assignUserRole(MemberRole memberRole) {
		this.getMemberRoles().add(memberRole);
	}
}
