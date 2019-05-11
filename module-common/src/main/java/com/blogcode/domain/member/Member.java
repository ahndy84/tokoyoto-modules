package com.blogcode.domain.member;

import com.blogcode.domain.enums.Grade;
import com.blogcode.domain.enums.UserStatus;
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

    private String name;

    private String password;

    private String email;

    private String principal;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @Builder
    public Member(String name, String password, String email, String principal, UserStatus status, Grade grade, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.principal = principal;
        this.status = status;
        this.grade = grade;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Member SetInactive() {
        status = UserStatus.INACTIVE;
        return this;
    }
}
