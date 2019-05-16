package com.salt.batch.jobs.inactive;

import com.salt.domain.member.enums.UserStatus;
import com.salt.domain.member.Member;
import com.salt.domain.member.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class InactiveItemTasklet implements Tasklet {
    private MemberRepository memberRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        log.info("------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tasklet!!!!");
        // reader
        Date nowDate = (Date) chunkContext.getStepContext().getJobParameters().get("nowdate");
        LocalDateTime now = LocalDateTime.ofInstant(nowDate.toInstant(), ZoneId.systemDefault());
        List<Member> inactiveMembers = memberRepository.findByUpdatedDateBeforeAndStatusEquals(now.minusYears(1), UserStatus.ACTIVE);

        // processor
        inactiveMembers = inactiveMembers.stream()
                .map(Member::setInactive) //스트림의 각 요소를 연산하는데 쓰인다. 여기선 각 요소별 setInactive 실행
                .collect(Collectors.toList()); //콜렉션 = 각 값을 모아주는 기능. toMap, toSet, toList로 해당 스트림을 다시 컬렉션으로 바꿔준다.

        //writer
        memberRepository.saveAll(inactiveMembers);

        return RepeatStatus.FINISHED;
    }
}
