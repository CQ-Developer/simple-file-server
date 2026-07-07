package com.chen.sfs.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

	private UUID id;
	private String name;
	private String hash;
	private Long size;
	private String uploader;
	private LocalDateTime uploadTime;

}
