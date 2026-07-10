package com.chen.sfs.service.impl;

import com.chen.sfs.exception.AppException;
import com.chen.sfs.repository.FilesRepository;
import com.chen.sfs.service.FileDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileDeleteServiceImpl implements FileDeleteService {

        private final FilesRepository repository;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void delete(String uploader, UUID id) {
                var opt = repository.findById(id);
                if (opt.isEmpty()) {
                        return;
                }
                var entity = opt.get();
                if (!uploader.equals(entity.getUploader())) {
                        return;
                }
                long cnt = repository.count(entity.getHash());
                if (cnt == 1) {
                        deleteFile(entity.getAbsolutePath());
                }
                if (cnt > 0) {
                        repository.deleteById(id);
                }
        }

        private void deleteFile(Path file) {
                try {
                        Files.deleteIfExists(file);
                } catch (IOException e) {
                        throw new AppException("Failed to delete file", e);
                }
        }

}
