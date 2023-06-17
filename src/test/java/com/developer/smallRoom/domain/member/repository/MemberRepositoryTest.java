package com.developer.smallRoom.domain.member.repository;

import com.developer.smallRoom.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private final String GITHUB_ID = "githubId";
    private final String NAME = "name";
    private final String IMAGE_URL = "imageUrl";

    @AfterEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @DisplayName("findByGitHubId() : 사용자 GitHubId를 통해 Member를 찾는다.")
    @Test
    void findByGitHubId() {
        //given
        Member savedMember = saveMember();
        //when
        Optional<Member> findMember = memberRepository.findByGitHubId(GITHUB_ID);
        //then
        Assertions.assertThat(findMember.get().getGitHubId()).isEqualTo(savedMember.getGitHubId());
    }

    private Member saveMember() {
        return memberRepository.save(Member.builder()
                .gitHubId(GITHUB_ID)
                .name(NAME)
                .imageUrl(IMAGE_URL).build());
    }
}