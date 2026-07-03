package com.chen.sfs.service;

import com.chen.sfs.repository.FilesRepository;
import com.chen.sfs.repository.jpa.entity.FilesEntity;
import com.chen.sfs.service.impl.FileDeleteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileDeleteServiceTest {

        FileDeleteService service;

        @Mock
	FilesRepository repository;

        @BeforeEach
        void setup() {
                service = new FileDeleteServiceImpl(repository);
        }

        @Test
        void delete_without_file() {
                when(repository.findById(any())).thenReturn(Optional.empty());

                service.delete("admin", UUID.randomUUID());

                verify(repository, times(1)).findById(any());
                verify(repository, never()).count(any());
                verify(repository, never()).deleteById(any());
        }

        @Test
        void delete_with_file(@TempDir Path path) throws IOException {
                var file = path.resolve("a.txt");
                Files.write(file, new byte[]{1}, CREATE);

                var entity = new FilesEntity();
                entity.setHash("1");
                entity.setAbsolutePath(file);
                entity.setUploader("admin");
                when(repository.findById(any())).thenReturn(Optional.of(entity));
                when(repository.count(anyString())).thenReturn(1L);
                doNothing().when(repository).deleteById(any());

                service.delete("admin", UUID.randomUUID());

                verify(repository, times(1)).findById(any());
                verify(repository, times(1)).count(any());
                verify(repository, times(1)).deleteById(any());
        }

}