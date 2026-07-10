package com.chen.sfs.service;

import com.chen.sfs.exception.AppException;
import com.chen.sfs.repository.FilesRepository;
import com.chen.sfs.repository.jpa.entity.FilesEntity;
import com.chen.sfs.service.impl.FileDownloadServiceImpl;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@ExtendWith(MockitoExtension.class)
class FileDownloadServiceTest {

        FileDownloadService service;

        @Mock
	FilesRepository repository;

        @BeforeEach
        void setup() {
                service = new FileDownloadServiceImpl(repository);
        }

        @Test
        void download_without_file_in_db() {
                when(repository.findById(any())).thenReturn(Optional.empty());

                assertThatThrownBy(() -> service.download(UUID.randomUUID())).isInstanceOf(AppException.class).hasMessage("File not found");

                verify(repository, times(1)).findById(any());
                verify(repository, never()).deleteById(any());
        }

        @Test
        void download_without_file_in_fs(@TempDir Path path) {
                var entity = new FilesEntity();
                entity.setAbsolutePath(path.resolve("/a.txt"));
                when(repository.findById(any())).thenReturn(Optional.of(entity));
                doNothing().when(repository).deleteById(any());

                assertThatThrownBy(() -> service.download(UUID.randomUUID())).isInstanceOf(AppException.class).hasMessage("File not found");

                verify(repository, times(1)).findById(any());
                verify(repository, times(1)).deleteById(any());
        }

        @Test
        void download_with_file(@TempDir Path path) throws IOException {
                var filename = "a.txt";
                var file = Files.write(path.resolve(filename), new byte[]{1}, CREATE);

                var entity = new FilesEntity();
                entity.setName(filename);
                entity.setMediaType(TEXT_PLAIN_VALUE);
                entity.setSize(1L);
                entity.setAbsolutePath(file);
                when(repository.findById(any())).thenReturn(Optional.of(entity));

                var fileInfo = service.download(UUID.randomUUID());
                assertThat(fileInfo.getName()).isEqualTo(filename);
                assertThat(fileInfo.getMediaType()).isEqualTo(TEXT_PLAIN_VALUE);
                assertThat(fileInfo.getSize()).isEqualTo(1L);
                assertThat(fileInfo.getAbsolutePath()).isEqualTo(file);

                verify(repository, times(1)).findById(any());
                verify(repository, never()).deleteById(any());
        }

}
