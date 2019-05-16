package com.salt.batch.jobs.decide.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.Random;

@Slf4j
public class OddDecider implements JobExecutionDecider {
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		Random rand = new Random();

		int randomNumber = rand.nextInt(50) + 1;

		log.info("---------------------- RANDOM NUMBER : {}", randomNumber);

		if(randomNumber % 2 == 0) return new FlowExecutionStatus("EVEN");
		else return new FlowExecutionStatus("ODD");
	}
}
