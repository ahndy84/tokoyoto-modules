package com.salt.batch.jobs.simpleNextCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleNextCondition {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job stepNextConditionalJob() {

        /*
        .on()
            캐치할 ExitStatus 지정
            * 일 경우 모든 ExitStatus가 지정된다.
        to()
            다음으로 이동할 Step 지정
        from()
            일종의 이벤트 리스너 역할
            상태값을 보고 일치하는 상태라면 to()에 포함된 step을 호출합니다.
            step1의 이벤트 캐치가 FAILED로 되있는 상태에서 추가로 이벤트 캐치하려면 from을 써야만 함
        end()
            end는 FlowBuilder를 반환하는 end와 FlowBuilder를 종료하는 end 2개가 있음
            on("*")뒤에 있는 end는 FlowBuilder를 반환하는 end
            build() 앞에 있는 end는 FlowBuilder를 종료하는 end
            FlowBuilder를 반환하는 end 사용시 계속해서 from을 이어갈 수 있음

        여기서 중요한 점은 on이 캐치하는 상태값이 BatchStatus가 아닌 ExitStatus라는 점입니다.
        그래서 분기처리를 위해 상태값 조정이 필요하시다면 ExitStatus를 조정해야합니다.
        조정하는 코드는 아래와 같습니다.
         */

        return jobBuilderFactory.get("stepNextConditionalJob")
            .start(conditionalJobStep1())
                .on("FAILED")
                .to(conditionalJobStep3())
                .on("*")
                .end()
            .from(conditionalJobStep1())
                .on("*")
                .to(conditionalJobStep2())
                .next(conditionalJobStep3())
                .on("*")
                .end()
            .end()
            .build();
    }

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get("conditionalJobStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>>>>>>>>>> conditionalJobStep1");
                    /*
                    ExitStatus를 FAILED로 지정한다.
                    해당 status를 보고 flow가 진행된다.
                     */
                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("conditionalJobStep2")
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>>>>>>>>>>>>>>>>>> conditionalJobStep2");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("conditionalJobStep3")
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>>>>>>>>>>>>>>>>>> conditionalJobStep3");
                return RepeatStatus.FINISHED;
            })
            .build();
    }
}
