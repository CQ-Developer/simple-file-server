package com.chen.sfs.controller.resp;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class FileInfoResp {

	private UUID id;
	private String name;
	private String hash;
	private Long size;
	private LocalDateTime uploadTime;

}
