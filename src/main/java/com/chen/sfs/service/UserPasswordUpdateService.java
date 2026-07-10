package com.chen.sfs.service;

public interface UserPasswordUpdateService {

	void updatePassword(String username, String oldPassword, String newPassword);

}
