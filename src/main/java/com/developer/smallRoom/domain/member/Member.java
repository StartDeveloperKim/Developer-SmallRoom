package com.developer.smallRoom.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Column(name = "gitHub_id", nullable = false)
    private String gitHubId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Builder
    public Member(String gitHubId, String name, String imageUrl) {
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.role = Role.USER;
    }

    public void update(Member member) {
        updateName(member.name);
        updateImageUrl(member.imageUrl);
    }

    private void updateImageUrl(String imageUrl) {
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    private void updateName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
