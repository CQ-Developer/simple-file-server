package com.chen.sfs.service;

public interface UserUpdateService {

	void updateUser(String username, Boolean disabled, Boolean locked, Boolean resetPassword);

}
