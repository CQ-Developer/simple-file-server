package com.chen.sfs.repository;

import com.chen.sfs.exception.AppException;
import com.chen.sfs.repository.jpa.UsersJpaRepository;
import com.chen.sfs.repository.jpa.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class UsersRepository {

	private final UsersJpaRepository jpaRepository;

	public Optional<UsersEntity> findById(String username) {
		if (isEmpty(username)) {
			return Optional.empty();
		}
		try {
			return jpaRepository.findById(username);
		} catch (Throwable e) {
			throw new AppException("Failed to query user information", e);
		}
	}

	public void save(UsersEntity entity) {
		if (isEmpty(entity) || isEmpty(entity.getUsername())) {
			return;
		}
		try {
			jpaRepository.save(entity);
		} catch (Throwable e) {
			throw new AppException("Failed to update user information", e);
		}
	}

	public boolean exists(String username) {
		if (isEmpty(username)) {
			return false;
		}
		try {
			return jpaRepository.existsById(username);
		} catch (Throwable e) {
			throw new AppException("Failed to check user exists", e);
		}
	}

}
