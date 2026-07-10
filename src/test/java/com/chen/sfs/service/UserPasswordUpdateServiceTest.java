package com.chen.sfs.service;

import com.chen.sfs.exception.AppException;
import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.repository.jpa.entity.UsersEntity;
import com.chen.sfs.service.impl.UserPasswordUpdateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion.$2B;

@ExtendWith(MockitoExtension.class)
class UserPasswordUpdateServiceTest {

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder($2B, 12);

	UserPasswordUpdateService userPasswordUpdateService;

	@Mock
	UsersRepository usersRepository;

	@BeforeEach
	void setup() {
		userPasswordUpdateService = new UserPasswordUpdateServiceImpl(passwordEncoder, usersRepository);
	}

	@Test
	void update_password_without_user() {
		when(usersRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThatCode(() -> userPasswordUpdateService.updatePassword("dummy", "", ""))
			.doesNotThrowAnyException();

		verify(usersRepository, never()).save(any());
	}

	@Test
	void update_password_with_wrong_password() {
		var user = new UsersEntity();
		user.setPassword(passwordEncoder.encode("123"));
		when(usersRepository.findById(anyString())).thenReturn(Optional.of(user));

		assertThatCode(() -> userPasswordUpdateService.updatePassword("dummy", "abc", ""))
			.isInstanceOf(AppException.class).hasMessage("Old password invalid");

		verify(usersRepository, never()).save(any());
	}

	@Test
	void update_password_ok() {
		var user = new UsersEntity();
		user.setPassword(passwordEncoder.encode("123"));
		when(usersRepository.findById(anyString())).thenReturn(Optional.of(user));
		doNothing().when(usersRepository).save(any());

		assertThatCode(() -> userPasswordUpdateService.updatePassword("dummy", "123", "abc")).doesNotThrowAnyException();

		verify(usersRepository, times(1)).save(any());
	}

}
