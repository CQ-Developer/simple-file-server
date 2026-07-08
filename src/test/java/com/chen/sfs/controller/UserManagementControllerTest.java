package com.chen.sfs.controller;

import com.chen.sfs.config.security.SecurityConfig;
import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.service.PasswordUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WithMockUser(username = "admin", password = "123")
@Import(SecurityConfig.class)
@WebMvcTest(UserManagementController.class)
class UserManagementControllerTest {

	@Autowired
	MockMvcTester mockMvcTester;

	@MockitoBean
	PasswordUpdateService passwordUpdateService;

	@MockitoBean
	UsersRepository usersRepository;

	@Test
	void update_password_without_new_password() {
		var result = mockMvcTester
			.patch()
			.uri("/api/user/password")
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
			.uri("/api/user/password")
			.contentType(APPLICATION_JSON)
			.content("{\"oldPassword\":\"123\",\"newPassword\":\"123456\"}")
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isTrue();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("ok");
	}

}
