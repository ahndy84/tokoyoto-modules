package com.blogcode.controller.member;

import com.blogcode.domain.member.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
	@GetMapping("/")
	public Member get () {
		return new Member("ahndy84", "ahndy84@gmail.com");
	}
}
