package com.chen.sfs.config.security;

import com.chen.sfs.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

	private final UsersRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var entity = repository.findById(username)
		                       .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new User(
			entity.getUsername(),
			entity.getPassword(),
			entity.getEnabled(),
			entity.getLastLoginTime().plusYears(1).isAfter(LocalDateTime.now()),
			entity.getLastPasswordTime().plusMonths(6).isAfter(LocalDateTime.now()),
			!entity.getLocked(),
			entity.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
		);
	}

}
