package com.chen.sfs.service.impl;

import static com.chen.sfs.mapper.FileInfoEntityMapper.INSTANCE;

import org.springframework.stereotype.Service;

import com.chen.sfs.repository.FilesRepository;
import com.chen.sfs.service.FilesService;
import com.chen.sfs.service.common.Page;
import com.chen.sfs.service.dto.FileInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {

	private final FilesRepository repository;

	@Override
	public Page<FileInfo> findFiles(String uploader, String name, Integer page, Integer size) {
		var files = repository
				.page(uploader, name, page - 1, size)
				.map(INSTANCE::toFileInfo);
		return Page.<FileInfo>builder()
				.pages(files.getTotalPages())
				.records(files.getContent())
				.build();
	}

}
