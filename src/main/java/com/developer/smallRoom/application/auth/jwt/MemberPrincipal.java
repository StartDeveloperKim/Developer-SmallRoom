package com.developer.smallRoom.application.auth.jwt;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@ToString
public class MemberPrincipal implements UserDetails {

    private final String memberGitHubId;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;

    public MemberPrincipal(String memberGitHubId, String role, Collection<? extends GrantedAuthority> authorities) {
        this.memberGitHubId = memberGitHubId;
        this.role = role;
        this.authorities = authorities;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberGitHubId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
