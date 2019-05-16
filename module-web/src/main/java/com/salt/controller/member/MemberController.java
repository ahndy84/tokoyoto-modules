package com.salt.controller.member;

import com.salt.domain.member.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
	@GetMapping("/")
	public Member get () {
		return new Member("ahndy84", "ahndy84@gmail.com");
	}
}
