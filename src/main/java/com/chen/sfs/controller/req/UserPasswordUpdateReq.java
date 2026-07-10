package com.chen.sfs.controller.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordUpdateReq {

	@NotBlank(message = "Old password can not be empty")
	private String oldPassword;

	@Size(min = 6, message = "Password must be at least 6 chars")
	@NotBlank(message = "New password can not be empty")
	private String newPassword;

}
