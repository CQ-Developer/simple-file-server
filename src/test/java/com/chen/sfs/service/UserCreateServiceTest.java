package com.chen.sfs.service;

import com.chen.sfs.exception.AppException;
import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.service.impl.UserCreateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
class UserCreateServiceTest {

	UserCreateService userCreateService;

	@Mock
	UsersRepository usersRepository;

	@BeforeEach
	void setup() {
		userCreateService = new UserCreateServiceImpl(new BCryptPasswordEncoder($2B, 12), usersRepository);
	}

	@Test
	void create_user_with_username_conflict() {
		when(usersRepository.exists(anyString())).thenReturn(true);

		assertThatCode(() -> userCreateService.createUser("admin", null))
			.isInstanceOf(AppException.class)
			.hasMessage("The username 'admin' is already taken");

		verify(usersRepository, times(1)).exists(anyString());
		verify(usersRepository, never()).save(any());
	}

	@Test
	void create_user() {
		when(usersRepository.exists(anyString())).thenReturn(false);
		doNothing().when(usersRepository).save(any());

		assertThatCode(() -> userCreateService.createUser("admin", "123"))
			.doesNotThrowAnyException();

		verify(usersRepository, times(1)).exists(anyString());
		verify(usersRepository, times(1)).save(any());
	}

}
