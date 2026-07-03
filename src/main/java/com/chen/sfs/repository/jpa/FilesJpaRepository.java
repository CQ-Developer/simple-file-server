package com.chen.sfs.repository.jpa;

import com.chen.sfs.repository.jpa.entity.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface FilesJpaRepository extends JpaRepository<FilesEntity, UUID>, JpaSpecificationExecutor<FilesEntity> {
}
