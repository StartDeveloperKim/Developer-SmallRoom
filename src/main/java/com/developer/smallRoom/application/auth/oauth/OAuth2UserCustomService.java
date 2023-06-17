package com.developer.smallRoom.application.auth.oauth;

import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        return saveOrUpdate(user);
    }

    private OAuth2User saveOrUpdate(OAuth2User oAuth2User) {
        CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(oAuth2User);
        Optional<Member> member = memberRepository.findByGitHubId(customOAuth2Member.getId());
        if (member.isEmpty()) {
            Member savedMember = memberRepository.save(customOAuth2Member.toMember());
            customOAuth2Member.setRole(savedMember.getRole());
        }else{
            // TODO :: 만약 사용자가 깃허브에서 닉네임을 변경했다면 여기서 Update 쿼리를 날려야 수정이 가능하다.
            customOAuth2Member.setRole(member.get().getRole());
        }
        return customOAuth2Member;
    }
}
