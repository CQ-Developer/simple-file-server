package com.chen.sfs.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.chen.sfs.exception.AppException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.chen.sfs.config.properties.SfsProperties;
import com.chen.sfs.repository.FilesRepository;
import com.chen.sfs.repository.jpa.entity.FilesEntity;
import com.chen.sfs.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

	private final Tika tika = new Tika();
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	private final SfsProperties properties;
	private final FilesRepository repository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void upload(String uploader, List<MultipartFile> files) {
		var now = LocalDateTime.now();
		var pre = createDatePath(now);
		var entities = new ArrayList<FilesEntity>();
		for (var file : files) {
			var hash = calculateHash(file);
			var name = evaluateFilename(hash, file.getOriginalFilename());
			if (repository.exists(uploader, hash, name)) {
				continue;
			}
			var media = detectMediaType(name, file);
			var abs = repository.findByHash(hash)
				.map(FilesEntity::getAbsolutePath)
				.orElseGet(() -> saveToFileSystem(file, hash, pre));
			entities.add(createFileInfo(name, hash, media, file.getSize(), abs, uploader, now));
		}
		repository.saveAll(entities);
	}

	private Path createDatePath(LocalDateTime now) {
		try {
			Path dir = properties
				.getPathPrefix()
				.resolve(formatter.format(now)).normalize()
				.toAbsolutePath();
			Files.createDirectories(dir);
			return dir;
		} catch (Throwable e) {
			throw new AppException("Failed to create parent date directory", e);
		}
	}

	private String calculateHash(MultipartFile file) {
		try {
			return DigestUtils.sha256Hex(file.getInputStream());
		} catch (Throwable e) {
			throw new AppException("Failed to calculate file hash", e);
		}
	}

	private String evaluateFilename(String hash, String name) {
		if (ObjectUtils.isEmpty(name)) {
			return hash;
		}
		return name;
	}

	private String detectMediaType(String name, MultipartFile file) {
		try {
			return tika.detect(file.getInputStream(), name);
		} catch (Throwable e) {
			throw new AppException("Failed to detect media type", e);
		}
	}

	private Path saveToFileSystem(MultipartFile file, String hash, Path pre) {
		try {
			var abs = pre.resolve(hash).normalize().toAbsolutePath();
			if (Files.notExists(abs)) {
				file.transferTo(abs);
			}
			return abs;
		} catch (Throwable e) {
			throw new AppException("Failed to save file to filesystem", e);
		}
	}

	private FilesEntity createFileInfo(
			String name, String hash, String media,
			Long size, Path abs, String uploader,
			LocalDateTime now) {
		var entity = new FilesEntity();
		entity.setName(name);
		entity.setHash(hash);
		entity.setMediaType(media);
		entity.setSize(size);
		entity.setAbsolutePath(abs);
		entity.setUploader(uploader);
		entity.setUploadTime(now);
		return entity;
	}

}
