package com.chen.sfs.controller;

import com.chen.sfs.controller.req.UserCreateReq;
import com.chen.sfs.controller.req.UserUpdateReq;
import com.chen.sfs.controller.resp.AppResp;
import com.chen.sfs.controller.req.UserPasswordUpdateReq;
import com.chen.sfs.service.UserCreateService;
import com.chen.sfs.service.UserPasswordUpdateService;
import com.chen.sfs.service.UserUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserManagementController {

	private final UserPasswordUpdateService userPasswordUpdateService;
	private final UserUpdateService userUpdateService;
	private final UserCreateService userCreateService;

	@PatchMapping("/me/password")
	public AppResp<Void> updatePassword(
			@AuthenticationPrincipal(expression = "username") String username,
			@Valid @RequestBody UserPasswordUpdateReq req) {
		userPasswordUpdateService.updatePassword(username, req.getOldPassword(), req.getNewPassword());
		return AppResp.ok();
	}

	@PutMapping("/{username}")
	public AppResp<Void> update(
			@PathVariable("username") String username,
			@Valid @RequestBody UserUpdateReq req) {
		userUpdateService.updateUser(username, req.getDisabled(), req.getLocked(), req.getResetPassword());
		return AppResp.ok();
	}

	@PostMapping
	public AppResp<Void> create(
			@Valid @RequestBody UserCreateReq req) {
		userCreateService.createUser(req.getUsername(), req.getPassword());
		return AppResp.ok();
	}

}
