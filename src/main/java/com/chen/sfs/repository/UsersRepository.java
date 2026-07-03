package com.chen.sfs.repository;

import com.chen.sfs.exception.DatabaseOperationException;
import com.chen.sfs.repository.jpa.UsersJpaRepository;
import com.chen.sfs.repository.jpa.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsersRepository {

	private final UsersJpaRepository jpaRepository;

	public Optional<UsersEntity> findUser(String username) {
		if (ObjectUtils.isEmpty(username)) {
			return Optional.empty();
		}
		try {
			return jpaRepository.findById(username);
		} catch (Throwable e) {
			throw new DatabaseOperationException("Failed to query user information", e);
		}
	}

}
