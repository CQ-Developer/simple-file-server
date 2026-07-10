package com.chen.sfs.service;

import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.repository.jpa.entity.UsersEntity;
import com.chen.sfs.service.impl.UserUpdateServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion.$2B;

@ExtendWith(MockitoExtension.class)
class UserUpdateServiceTest {

	UserUpdateService userUpdateService;

	@Mock
	UsersRepository usersRepository;

	@BeforeEach
	void setup() {
		userUpdateService = new UserUpdateServiceImpl(new BCryptPasswordEncoder($2B, 12), usersRepository);
	}

	@Test
	void update_user() {
		var user = new UsersEntity();
		when(usersRepository.findById(anyString())).thenReturn(Optional.of(user));
		doNothing().when(usersRepository).save(any());

		Assertions.assertThatCode(() -> userUpdateService.updateUser("admin", true, true, true)).doesNotThrowAnyException();

		verify(usersRepository, times(1)).findById(anyString());
		verify(usersRepository, times(1)).save(any());
	}

}
