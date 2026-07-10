package com.chen.sfs.service.impl;

import com.chen.sfs.exception.AppException;
import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.service.UserPasswordUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPasswordUpdateServiceImpl implements UserPasswordUpdateService {

	private final PasswordEncoder passwordEncoder;
	private final UsersRepository usersRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updatePassword(String username, String oldPassword, String newPassword) {
		var opt = usersRepository.findById(username);
		if (opt.isEmpty()) {
			return;
		}
		var user = opt.get();
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new AppException("Old password invalid");
		}
		var encoded = passwordEncoder.encode(newPassword);
		user.setPassword(encoded);
		usersRepository.save(user);
	}

}
