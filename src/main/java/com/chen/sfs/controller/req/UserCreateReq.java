package com.chen.sfs.controller.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateReq {

	@NotBlank(message = "Username can not be empty")
	@Size(min = 6, message = "Username must be at least 6 chars")
	private String username;

	@NotBlank(message = "Password can not be empty")
	@Size(min = 6, message = "Password must be at least 6 chars")
	private String password;

}
