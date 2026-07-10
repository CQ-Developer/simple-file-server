package com.chen.sfs.service.impl;

import com.chen.sfs.exception.AppException;
import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.repository.jpa.entity.UsersEntity;
import com.chen.sfs.service.UserCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCreateServiceImpl implements UserCreateService {

	private final PasswordEncoder passwordEncoder;
	private final UsersRepository usersRepository;

	@Override
	public void createUser(String username, String password) {
		if (usersRepository.exists(username)) {
			throw new AppException("The username '" + username + "' is already taken");
		}
		var now = LocalDateTime.now();
		var encoded = passwordEncoder.encode(password);
		var authorities = List.of(
			"sfs:files:upload",
			"sfs:files:download",
			"sfs:files:delete",
			"sfs:files:list",
			"sfs:users:password"
		);
		var user = new UsersEntity();
		user.setUsername(username);
		user.setPassword(encoded);
		user.setEnabled(true);
		user.setLocked(false);
		user.setLastLoginTime(now);
		user.setLastPasswordTime(now);
		user.setAuthorities(authorities);
		usersRepository.save(user);
	}

}
