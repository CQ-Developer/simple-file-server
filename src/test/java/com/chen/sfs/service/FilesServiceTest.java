package com.chen.sfs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import com.chen.sfs.repository.jpa.entity.FilesEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import com.chen.sfs.repository.FilesRepository;
import com.chen.sfs.service.impl.FilesServiceImpl;

@ExtendWith(MockitoExtension.class)
class FilesServiceTest {

	FilesService filesService;

	@Mock
	FilesRepository repository;

	@BeforeEach
	void setup() {
		filesService = new FilesServiceImpl(repository);
	}

	@Test
	void find_files() {
		var entity = new FilesEntity();
		var page = new PageImpl<>(List.of(entity));
		when(repository.page(anyString(), anyString(), anyInt(), anyInt())).thenReturn(page);

		var result = filesService.findFiles("admin", "a", 1, 10);

		assertThat(result).isNotNull();
		assertThat(result.getPages()).isEqualTo(1);
		assertThat(result.getRecords()).hasSize(1);
	}

}
