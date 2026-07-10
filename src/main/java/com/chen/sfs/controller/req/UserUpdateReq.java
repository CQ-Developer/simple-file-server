package com.chen.sfs.controller.req;

import jakarta.validation.constraints.AssertFalse;
import lombok.Data;

@Data
public class UserUpdateReq {

	private Boolean resetPassword;
	private Boolean disabled;
	private Boolean locked;

	@AssertFalse(message = "Must contains at least one property")
	public boolean isValid() {
		return resetPassword == null && disabled == null && locked == null;
	}

}
