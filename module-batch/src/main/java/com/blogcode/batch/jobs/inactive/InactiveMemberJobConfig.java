package com.blogcode.batch.jobs.inactive;

import com.blogcode.batch.jobs.inactive.listener.InactiveJobExecutionDecider;
import com.blogcode.batch.jobs.inactive.listener.InactiveJobListener;
import com.blogcode.batch.jobs.inactive.listener.InactiveStepListener;
import com.blogcode.domain.member.enums.UserStatus;
import com.blogcode.domain.member.Member;
import com.blogcode.domain.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Configuration
public class InactiveMemberJobConfig {
    private final static int CHUNK_SIZE = 15;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    private MemberRepository memberRepository;

    @Bean
    public Job inactiveMemberJob(JobBuilderFactory jobBuilderFactory, InactiveJobListener inactiveJobListener, Flow inactiveJobFlow /*,Step inactiveJobStep*/) {
        return jobBuilderFactory.get("inactiveMemberJob")
                .preventRestart()
                .listener(inactiveJobListener)
                //.start(inactiveJobStep)
                .start(inactiveJobFlow)
                .end()
                .build();
    }

    @Bean
    public Step inactiveJobStep(StepBuilderFactory stepBuilderFactory, JpaPagingItemReader<Member> inactiveMemberJpaReader, InactiveStepListener inactiveStepListener) {
        return stepBuilderFactory.get("inactiveMemberStep")
                .<Member, Member> chunk(10) //<입력타입, 출력타입> 커밋단위가 10개
                .reader(inactiveMemberJpaReader) //데이터를 DB에서 읽어와 QueueItemReader에 저장
                .processor(inactiveMemberProcessor()) //휴면회원 전환 처리
                .writer(inactiveMemberWriter()) //휴면회원을 DB에 저장
                .listener(inactiveStepListener)
                .build();
    }

    @Bean
    public Flow inactiveJobFlow(Step inactiveJobStep) {
        FlowBuilder<Flow> flowbuilder = new FlowBuilder<>("inactiveJobFlow");
        return flowbuilder
                .start(new InactiveJobExecutionDecider())
                .on(FlowExecutionStatus.FAILED.getName()).end()
                .on(FlowExecutionStatus.COMPLETED.getName()).to(inactiveJobStep)
                .end();
    }


//    @Bean
//    @StepScope
//    public QueueItemReader<Member> inactiveMemberReader() {
//        List<Member> oldMembers =
//                memberRepository.findByUpdatedDateBeforeAndStatusEquals(LocalDateTime.now().minusYears(1), UserStatus.ACTIVE);
//
//        return new QueueItemReader<>(oldMembers);
//    }

//    @Bean
//    @StepScope
//    public ListItemReader<Member> inactiveMemberReader(@Value("#{jobParameters[nowDate]}") Date nowdate, MemberRepository memberRepository) {
//        List<Member> oldMembers =
//                memberRepository.findByUpdatedDateBeforeAndStatusEquals(LocalDateTime.now().minusYears(1), UserStatus.ACTIVE);
//        return new ListItemReader<>(oldMembers);
//    }

    @Bean(destroyMethod="")
    @StepScope
    public JpaPagingItemReader<Member> inactiveMemberJpaReader(@Value("#{jobParameters[nowDate]}") Date nowDate)  {
        JpaPagingItemReader<Member> jpaPagingItemReader = new JpaPagingItemReader<>();
        jpaPagingItemReader.setQueryString("select m from Member as m where m.updatedDate <:updatedDate and m.status = :status");

        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        //LocalDateTime now = LocalDateTime.ofInstant(nowDate.toInstant(), ZoneId.systemDefault());

        map.put("updatedDate", now.minusYears(1));
        map.put("status", UserStatus.ACTIVE);

        jpaPagingItemReader.setParameterValues(map);
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
        jpaPagingItemReader.setPageSize(CHUNK_SIZE);

        return jpaPagingItemReader;
    }

    public ItemProcessor<Member, Member> inactiveMemberProcessor() {
        return member -> member.setInactive();
    }

//    public ItemWriter<Member> inactiveMemberWriter() {
//        return ((List<? extends Member> members) -> memberRepository.saveAll(members));
//    }

    public JpaItemWriter<Member> inactiveMemberWriter() {
        /*
            JpaItemWriter는 별도로 저장설정을 할 필요없이 제네릭에 저장할 타입을 명시하고 entityManagerFactory만 설정하면
            Processor에서 넘어온 데이터를 청크 단위로 저장합니다.
         */
        JpaItemWriter<Member> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
