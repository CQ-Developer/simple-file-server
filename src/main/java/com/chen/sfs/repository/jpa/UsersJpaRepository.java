package com.chen.sfs.repository.jpa;

import com.chen.sfs.repository.jpa.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsersJpaRepository extends JpaRepository<UsersEntity, String>, JpaSpecificationExecutor<UsersEntity> {
}
