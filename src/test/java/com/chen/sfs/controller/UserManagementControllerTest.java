package com.chen.sfs.controller;

import com.chen.sfs.config.security.SecurityConfig;
import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.service.UserCreateService;
import com.chen.sfs.service.UserPasswordUpdateService;
import com.chen.sfs.service.UserUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WithMockUser(
	username = "admin",
	password = "123",
	authorities = {
		"sfs:users:password",
		"sfs:users:update",
		"sfs:users:create"
	}
)
@Import(SecurityConfig.class)
@WebMvcTest(UserManagementController.class)
class UserManagementControllerTest {

	@Autowired
	MockMvcTester mockMvcTester;

	@MockitoBean
	UserPasswordUpdateService userPasswordUpdateService;

	@MockitoBean
	UserUpdateService userUpdateService;

	@MockitoBean
	UserCreateService userCreateService;

	@MockitoBean
	UsersRepository usersRepository;

	@Test
	void update_password_with_invalidate_params() {
		var result = mockMvcTester
			.patch()
			.uri("/api/users/me/password")
			.contentType(APPLICATION_JSON)
			.content("{\"oldPassword\":\"123\"}")
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isFalse();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("Request parameters error");
	}

	@Test
	void update_password_ok() {
		var result = mockMvcTester
			.patch()
			.uri("/api/users/me/password")
			.contentType(APPLICATION_JSON)
			.content("{\"oldPassword\":\"123\",\"newPassword\":\"123456\"}")
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isTrue();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("ok");
	}

	@Test
	void update_with_invalidate_params() {
		var result = mockMvcTester
			.put()
			.uri("/api/users/{username}", "admin")
			.contentType(APPLICATION_JSON)
			.content("{}")
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isFalse();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("Request parameters error");
	}

	@Test
	void update_ok() {
		doNothing().when(userUpdateService).updateUser(anyString(), anyBoolean(), isNull(), isNull());

		var result = mockMvcTester
			.put()
			.uri("/api/users/{username}", "admin")
			.contentType(APPLICATION_JSON)
			.content("{\"disabled\":true}")
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isTrue();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("ok");
	}

	@Test
	void create_with_invalidate_params() {
		var result = mockMvcTester
			.post()
			.uri("/api/users")
			.contentType(APPLICATION_JSON)
			.content("{\"username\":\"Tom\"}")
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isFalse();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("Request parameters error");
	}

	@Test
	void create_ok() {
		doNothing().when(userCreateService).createUser(anyString(), anyString());

		var result = mockMvcTester
			.post()
			.uri("/api/users")
			.contentType(APPLICATION_JSON)
			.content("{\"username\":\"JackChen\",\"password\":\"123456\"}")
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isTrue();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("ok");
	}

}
