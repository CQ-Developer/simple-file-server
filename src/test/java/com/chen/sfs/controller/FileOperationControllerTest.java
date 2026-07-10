package com.chen.sfs.controller;

import com.chen.sfs.config.security.SecurityConfig;
import com.chen.sfs.repository.UsersRepository;
import com.chen.sfs.service.FileDeleteService;
import com.chen.sfs.service.FileDownloadService;
import com.chen.sfs.service.FileUploadService;
import com.chen.sfs.service.FilesService;
import com.chen.sfs.service.common.Page;
import com.chen.sfs.service.dto.FileDownload;
import com.chen.sfs.service.dto.FileInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@WithMockUser(
	username = "admin",
	authorities = {
		"sfs:files:upload",
		"sfs:files:download",
		"sfs:files:delete",
		"sfs:files:list"
	}
)
@Import(SecurityConfig.class)
@WebMvcTest(FileOperationController.class)
class FileOperationControllerTest {

	@Autowired
	MockMvcTester mockMvcTester;

	@MockitoBean
	FilesService filesService;

	@MockitoBean
	FileUploadService fileUploadService;

	@MockitoBean
	FileDownloadService fileDownloadService;

	@MockitoBean
	FileDeleteService fileDeleteService;

	@MockitoBean
	UsersRepository usersRepository;

	@Test
	void upload_without_file() {
		var result = mockMvcTester
			.post()
			.uri("/api/files/upload")
			.multipart()
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isFalse();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("Missing form data");
		assertThat(result).bodyJson().doesNotHavePath("$.data");
	}

	@Test
	void upload_with_files() {
		doNothing().when(fileUploadService).upload(anyString(), anyList());

		var result = mockMvcTester
			.post()
			.uri("/api/files/upload")
			.multipart()
			.file("files", new byte[0])
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isTrue();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("ok");
		assertThat(result).bodyJson().doesNotHavePath("$.data");
	}

	@Test
	void download(@TempDir Path path) throws IOException {
		var file = path.resolve("a.txt");
		Files.write(file, new byte[]{1}, CREATE);

		when(fileDownloadService.download(any())).thenReturn(
			FileDownload.builder().name("a.txt").mediaType(TEXT_PLAIN_VALUE).size(1L).absolutePath(file).build()
		);

		var result = mockMvcTester
			.get()
			.uri("/api/files/{id}/download", UUID.randomUUID())
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).hasContentType(TEXT_PLAIN_VALUE);
		assertThat(result).hasHeader(CONTENT_LENGTH, "1");
	}

	@Test
	void delete() {
		doNothing().when(fileDeleteService).delete(anyString(), any());

		var result = mockMvcTester
			.delete()
			.uri("/api/files/{id}", UUID.randomUUID())
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isTrue();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("ok");
		assertThat(result).bodyJson().doesNotHavePath("$.data");
	}

	@Test
	void list() {
		var file = FileInfo.builder().name("a.txt").build();
		var page = Page.<FileInfo>builder().pages(1).records(List.of(file)).build();
		when(filesService.findFiles(anyString(), any(), anyInt(), anyInt())).thenReturn(page);

		var result = mockMvcTester
			.get()
			.uri("/api/files")
			.queryParam("name", "a")
			.exchange();

		assertThat(result).hasStatusOk();
		assertThat(result).bodyJson().extractingPath("$.success").asBoolean().isTrue();
		assertThat(result).bodyJson().extractingPath("$.message").asString().isEqualTo("ok");
		assertThat(result).bodyJson().extractingPath("$.data.pages").asNumber().isEqualTo(1);
		assertThat(result).bodyJson().extractingPath("$.data.records").asArray().hasSize(1);
	}

}
