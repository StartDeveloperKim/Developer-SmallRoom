package com.developer.smallRoom.application.auth.oauth;

import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.Role;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomOAuth2Member implements OAuth2User{

    private final String id;
    private final String name;
    private final String imageUrl;

    private Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public CustomOAuth2Member(Map<String, Object> attributes) {
        this.attributes = attributes;

        this.id = (String) attributes.get("login");
        this.name = (String) attributes.get("name");
        this.imageUrl = (String) attributes.get("avatar_url");
    }

    public Member toMember() {
        return Member.builder()
                .gitHubId(this.id)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .build();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setRole(Role role) {
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(role.getKey()));
    }

    public String getRole() {
        return authorities.iterator().next().getAuthority();
    }
}
