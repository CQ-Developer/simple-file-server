package com.chen.sfs.service.impl;

import java.nio.file.Files;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chen.sfs.exception.FileDownloadException;
import com.chen.sfs.mapper.FileInfoEntityMapper;
import com.chen.sfs.repository.FilesRepository;
import com.chen.sfs.service.FileDownloadService;
import com.chen.sfs.service.dto.FileDownload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileDownloadServiceImpl implements FileDownloadService {

        private final FilesRepository repository;

        @Override
        public FileDownload download(UUID id) {
                var entity = repository.findById(id)
                        .orElseThrow(() -> new FileDownloadException("File not found"));
                var file = entity.getAbsolutePath();
                if (Files.notExists(file)) {
                        repository.deleteById(id);
                        throw new FileDownloadException("File not found");
                }
                return FileInfoEntityMapper.INSTANCE.toFileDownload(entity);
        }

}
