package com.example.backend.global.auth.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.backend.domain.member.dto.MemberInfoDto;

import lombok.RequiredArgsConstructor;

/**
 * CustomUserDetails
 * <p></p>
 * @author 100minha
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

	private final MemberInfoDto memberInfoDto;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return memberInfoDto.nickname();
	}

	public Long getUserId() {
		return memberInfoDto.id();
	}

	public String getEmail() {
		return memberInfoDto.email();
	}

	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
}
