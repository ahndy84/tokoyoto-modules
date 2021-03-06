package com.salt.service;

import com.salt.domain.member.Member;
import com.salt.domain.member.MemberRepository;
import com.salt.service.dto.OrderDto;
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

	@Autowired
	private MemberRepository memberRepository;

	public MemberServiceCustom(RestTemplateBuilder restTemplateBuilder) {
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
