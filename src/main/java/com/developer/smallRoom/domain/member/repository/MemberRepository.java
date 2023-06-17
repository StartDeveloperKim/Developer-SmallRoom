package com.developer.smallRoom.domain.member.repository;

import com.developer.smallRoom.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByGitHubId(String gitHubId);

    boolean existsByGitHubId(String gitHubId);
}
