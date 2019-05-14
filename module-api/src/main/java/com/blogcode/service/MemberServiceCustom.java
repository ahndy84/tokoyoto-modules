package com.blogcode.service;

import com.blogcode.domain.member.Member;
import com.blogcode.domain.member.MemberRepository;
import com.blogcode.service.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 14.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Slf4j
@Service
public class MemberServiceCustom {


	private final RestTemplate restTemplate;

	@Value("${order.api.url}")
	private String orderApiUrl;

	private MemberRepository memberRepository;

	public MemberServiceCustom(MemberRepository memberRepository, RestTemplateBuilder restTemplateBuilder) {
		this.memberRepository = memberRepository;
		this.restTemplate = restTemplateBuilder.build();
	}

	public OrderDto getUserOrder(String orderNo) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderApiUrl)
				.queryParam("orderNo", orderNo);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
		return restTemplate.exchange(
				builder.toUriString(),
				HttpMethod.GET,
				httpEntity,
				OrderDto.class).getBody();
	}

	public Long signup (Member member) {
		return memberRepository.save(member).getIdx();
	}
}
