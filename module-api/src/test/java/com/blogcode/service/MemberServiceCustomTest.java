package com.blogcode.service;

import com.blogcode.service.dto.OrderDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;


@RestClientTest(MemberServiceCustom.class)
public class MemberServiceCustomTest {

	@Autowired
	private MemberServiceCustom memberServiceCustom;

	@Autowired
	private MockRestServiceServer mockRestServiceServer;

	private String orderApiUrl = "http://localhost:8090/order?orderNo=";

	@Test
	public void OrderDto는OrderAPI의Json결과값을담을수있다 () {

		//given
		String expectOrderNo = "1";
		Long expectAmount = 1000L;
		LocalDateTime expectOrderDateTime = LocalDateTime.of(2018, 9, 29, 0, 0);

		String expectResult =  "{\"orderNo\":\"1\",\"amount\":1000,\"orderDateTime\":\"2018-09-29 00:00:00\"}";
		mockRestServiceServer.expect(requestTo(orderApiUrl+expectOrderNo))
			.andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

		//when
		OrderDto response = memberServiceCustom.getUserOrder(expectOrderNo);

		//then
		assertThat(response.getOrderNo(), is(expectOrderNo));
		assertThat(response.getAmount(), is(expectAmount));
		assertThat(response.getOrderDateTime(), is(expectOrderDateTime));
	}
}
