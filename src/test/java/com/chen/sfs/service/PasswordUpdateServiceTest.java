package com.chen.sfs.service;

import com.chen.sfs.exception.PasswordUpdateException;
import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.repository.jpa.entity.UsersEntity;
import com.chen.sfs.service.impl.PasswordUpdateServiceImpl;
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
class PasswordUpdateServiceTest {

	final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder($2B, 12);

	PasswordUpdateService passwordUpdateService;

	@Mock
	UsersRepository usersRepository;

	@BeforeEach
	void setup() {
		passwordUpdateService = new PasswordUpdateServiceImpl(passwordEncoder, usersRepository);
	}

	@Test
	void update_password_without_user() {
		when(usersRepository.findUser(anyString())).thenReturn(Optional.empty());

		assertThatCode(() -> passwordUpdateService.updatePassword("dummy", "", ""))
			.doesNotThrowAnyException();

		verify(usersRepository, never()).update(any());
	}

	@Test
	void update_password_with_wrong_password() {
		var user = new UsersEntity();
		user.setPassword(passwordEncoder.encode("123"));
		when(usersRepository.findUser(anyString())).thenReturn(Optional.of(user));

		assertThatCode(() -> passwordUpdateService.updatePassword("dummy", "abc", ""))
			.isInstanceOf(PasswordUpdateException.class).hasMessage("Old password invalid");

		verify(usersRepository, never()).update(any());
	}

	@Test
	void update_password_ok() {
		var user = new UsersEntity();
		user.setPassword(passwordEncoder.encode("123"));
		when(usersRepository.findUser(anyString())).thenReturn(Optional.of(user));
		doNothing().when(usersRepository).update(any());

		assertThatCode(() -> passwordUpdateService.updatePassword("dummy", "123", "abc")).doesNotThrowAnyException();

		verify(usersRepository, times(1)).update(any());
	}

}
