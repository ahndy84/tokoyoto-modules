package com.salt.resolver;

import com.salt.domain.member.Member;
import com.salt.domain.member.MemberRepository;
import com.salt.domain.member.enums.SocialType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.salt.domain.member.enums.SocialType.*;

@Component
public class MemberargumentResolver implements HandlerMethodArgumentResolver {
	
	@Autowired
	private MemberRepository memberRepository;
	
	private Member getMember(Member member, HttpSession session) {
		if(member == null) {
			try {
				OAuth2AuthenticationToken authentication =
					(OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
				
				Map<String, Object> map = authentication.getPrincipal().getAttributes();
				
				Member converterMember = convertMember(authentication.
					getAuthorizedClientRegistrationId(), map);
				
				member = memberRepository.findByEmail(converterMember.getEmail());
				if(member == null) {
					member = memberRepository.save(converterMember);
				}
				setRoleIfNotSame("member", authentication, map);
				session.setAttribute("member", member);
			}catch(ClassCastException e) {
				return member;
			}
		}
		
		return member;
	}
	
	private Member convertMember(String authority, Map<String, Object> map) {
		if(FACEBOOK.getValue().equals(authority)) return getModernMember(FACEBOOK, map);
		else if(GOOGLE.getValue().equals(authority)) return getModernMember(GOOGLE, map);
		else if(KAKAO.getValue().equals(authority)) return getKakaoMember(KAKAO, map);
		return null;
	}
	
	private Member getModernMember(SocialType socialType, Map<String, Object> map) {
		return Member.builder()
			.name(String.valueOf(map.get("name")))
			.email(String.valueOf(map.get("email")))
			.socialType(socialType)
			.createdDate(LocalDateTime.now())
			.build();
	}
	
	private Member getKakaoMember(SocialType socialType, Map<String, Object>map) {
		HashMap<String, String> propertyMap = (HashMap<String, String>) map.get("properties");
		return Member.builder()
			.name(propertyMap.get("nickname"))
			.email(String.valueOf(map.get("kaccount_email")))
			.socialType(KAKAO)
			.createdDate(LocalDateTime.now())
			.build();
	}
	
	private void setRoleIfNotSame(Member member, OAuth2AuthenticationToken authentication, Map<String, Object> map) {
		if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority(
			member.getSocialType().getRoleType()))) {
			
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.
				createAuthorityList(member.getSocialType().getRoleType())));
		}
	}
}
