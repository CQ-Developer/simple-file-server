package com.chen.sfs.controller;

import com.chen.sfs.controller.common.AppResp;
import com.chen.sfs.controller.req.PasswordUpdateReq;
import com.chen.sfs.service.PasswordUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserManagementController {

	private final PasswordUpdateService passwordUpdateService;

	@PatchMapping("/password")
	public AppResp<Void> updatePassword(
			@AuthenticationPrincipal(expression = "username") String username,
			@Valid @RequestBody PasswordUpdateReq req) {
		passwordUpdateService.updatePassword(username, req.getOldPassword(), req.getNewPassword());
		return AppResp.ok();
	}

}
