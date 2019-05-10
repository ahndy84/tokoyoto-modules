package com.blogcode.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Member {
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	@Column
	private String email;

	public Member(String name, String email) {
		this.name = name;
		this.email = email;
	}
}
