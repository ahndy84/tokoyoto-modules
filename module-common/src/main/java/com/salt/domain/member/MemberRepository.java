package com.salt.domain.member;

import com.salt.domain.member.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUpdatedDateBeforeAndStatusEquals(LocalDateTime localDateTime, MemberStatus status);}
