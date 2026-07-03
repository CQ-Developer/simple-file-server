package com.chen.sfs.service;

import com.chen.sfs.config.properties.SfsProperties;
import com.chen.sfs.repository.FilesRepository;
import com.chen.sfs.service.impl.FileUploadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceTest {

	FileUploadService service;

	@Mock
	FilesRepository repository;

	@BeforeEach
	void setup(@TempDir Path p) {
		SfsProperties properties = new SfsProperties(p);
		service = new FileUploadServiceImpl(properties, repository);
	}

	@Test
	void upload_without_file() {
		assertThatCode(() -> service.upload("admin", List.of())).doesNotThrowAnyException();

		verify(repository, never()).exists(anyString(), anyString(), anyString());
		verify(repository, times(1)).saveAll(anyList());
	}

	@Test
	void upload_with_file() {
		when(repository.exists(anyString(), anyString(), anyString())).thenReturn(false, false, true);
		doNothing().when(repository).saveAll(anyList());

		var files = List.<MultipartFile>of(
			new MockMultipartFile("f", "a.txt", null, new byte[]{1}),
			new MockMultipartFile("f", null, null, new byte[]{2}),
			new MockMultipartFile("f", null, null, new byte[0])
		);
		assertThatCode(() -> service.upload("admin", files)).doesNotThrowAnyException();

		verify(repository, times(3)).exists(anyString(), anyString(), anyString());
		verify(repository, times(1)).saveAll(anyList());
	}

}
