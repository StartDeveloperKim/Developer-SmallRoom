package com.developer.smallRoom.domain.member.repository;

import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.factory.MemberFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

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
        Optional<Member> findMember = memberRepository.findByGitHubId(savedMember.getGitHubId());
        //then
        assertThat(findMember.get().getGitHubId()).isEqualTo(savedMember.getGitHubId());
    }

    private Member saveMember() {
        return memberRepository.save(MemberFactory.getMemberDefaultValue());
    }
}