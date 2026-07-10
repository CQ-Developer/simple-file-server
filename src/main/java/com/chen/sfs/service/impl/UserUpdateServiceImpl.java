package com.chen.sfs.service.impl;

import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateServiceImpl implements UserUpdateService {

	private final PasswordEncoder passwordEncoder;
	private final UsersRepository usersRepository;

	@Override
	public void updateUser(String username, Boolean disabled, Boolean locked, Boolean resetPassword) {
		var opt = usersRepository.findById(username);
		if (opt.isEmpty()) {
			return;
		}
		var user = opt.get();
		if (disabled != null) {
			user.setEnabled(disabled);
		}
		if (locked != null) {
			user.setLocked(locked);
		}
		if (resetPassword != null) {
			var encoded = passwordEncoder.encode("123456");
			user.setPassword(encoded);
		}
		usersRepository.save(user);
	}

}
