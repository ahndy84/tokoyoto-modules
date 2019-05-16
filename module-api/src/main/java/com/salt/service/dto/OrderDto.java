package com.salt.service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
	private String orderNo;

	private Long amount;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime orderDateTime;

	@Builder
	public OrderDto(String orderNo, Long amount, LocalDateTime orderDateTime) {
		this.orderNo = orderNo;
		this.amount = amount;
		this.orderDateTime = orderDateTime;
	}
}
