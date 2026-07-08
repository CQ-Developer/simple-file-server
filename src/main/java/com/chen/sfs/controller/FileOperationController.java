package com.chen.sfs.controller;

import com.chen.sfs.controller.common.AppResp;
import com.chen.sfs.controller.common.PageResp;
import com.chen.sfs.controller.resp.FileInfoResp;
import com.chen.sfs.mapper.FileInfoMapper;
import com.chen.sfs.service.FileDeleteService;
import com.chen.sfs.service.FileDownloadService;
import com.chen.sfs.service.FileUploadService;
import com.chen.sfs.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileOperationController {

	private final FilesService filesService;
	private final FileUploadService fileUploadService;
	private final FileDownloadService fileDownloadService;
	private final FileDeleteService fileDeleteService;

	@PostMapping("/upload")
	public AppResp<Void> upload(
		@AuthenticationPrincipal(expression = "username") String uploader,
		@RequestPart("files") List<MultipartFile> files) {
		fileUploadService.upload(uploader, files);
		return AppResp.ok();
	}

	@GetMapping("/{id}/download")
	public ResponseEntity<Resource> download(@PathVariable("id") UUID id) {
		var file = fileDownloadService.download(id);
		var disposition = ContentDisposition
			.attachment()
			.filename(file.getName(), UTF_8)
			.build()
			.toString();
		return ResponseEntity
			.ok()
			.contentType(MediaType.valueOf(file.getMediaType()))
			.contentLength(file.getSize())
			.header(CONTENT_DISPOSITION, disposition)
			.body(new FileSystemResource(file.getAbsolutePath()));
	}

	@DeleteMapping("/{id}")
	public AppResp<Void> delete(
		@AuthenticationPrincipal(expression = "username") String uploader,
		@PathVariable("id") UUID id) {
		fileDeleteService.delete(uploader, id);
		return AppResp.ok();
	}

	@GetMapping
	public AppResp<PageResp<FileInfoResp>> find(
		@AuthenticationPrincipal(expression = "username") String uploader,
		@RequestParam(name = "name", required = false) String name,
		@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
		@RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
		var files = filesService.findFiles(uploader, name, page, size);
		var pages = files.getPages();
		var records = FileInfoMapper.INSTANCE.toFileInfoResps(files.getRecords());
		return AppResp.ok(new PageResp<>(pages, records));
	}

}
