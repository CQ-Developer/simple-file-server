package com.chen.sfs.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(schema = "sfs", name = "files")
public class FilesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", columnDefinition = "uuid", nullable = false)
	private UUID id;

	@Column(name = "name", columnDefinition = "text", nullable = false)
	private String name;

	@Column(name = "hash", columnDefinition = "text", nullable = false)
	private String hash;

	@Column(name = "media_type", columnDefinition = "text", nullable = false)
	private String mediaType;

	@Column(name = "size", columnDefinition = "bigint", nullable = false)
	private Long size;

	@Column(name = "absolute_path", columnDefinition = "text", nullable = false)
	private Path absolutePath;

	@Column(name = "uploader", columnDefinition = "text", nullable = false)
	private String uploader;

	@Column(name = "upload_time", columnDefinition = "timestamp", nullable = false)
	private LocalDateTime uploadTime;

}
