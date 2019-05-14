package com.blogcode.domain.member;

import com.blogcode.domain.member.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUpdatedDateBeforeAndStatusEquals(LocalDateTime localDateTime, UserStatus status);}
