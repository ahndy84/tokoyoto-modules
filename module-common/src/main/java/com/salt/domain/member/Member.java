package com.salt.domain.member;

import com.salt.domain.member.enums.Grade;
import com.salt.domain.member.enums.SocialType;
import com.salt.domain.member.enums.UserStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = {"idx", "email"})
@NoArgsConstructor
@Entity
@Table
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String email;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private UserStatus status;  //회원상태

    @Enumerated(EnumType.STRING)
    private Grade grade;  //회원등급

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @Builder
    public Member(String email, String name, String password, SocialType socialType, UserStatus status, Grade grade, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.socialType = socialType;
        this.status = status;
        this.grade = grade;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Member setInactive() {
        status = UserStatus.INACTIVE;
        return this;
    }

    public Member (String name, String email){
        this.name = name;
        this.email = email;
    }
}
