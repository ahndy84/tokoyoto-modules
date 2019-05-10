package com.blogcode.service;

import com.blogcode.domain.Member;
import com.blogcode.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceCustom {
	private MemberRepositry memberRepositry;

	public MemberServiceCustom(MemberRepository memberRepository) {
		this.memberRepositry = memberRepository;
	}

	public Long signup (Member member) {
		return memberRepositry.save(member).getId();
	}
}
