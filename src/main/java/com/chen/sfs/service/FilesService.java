package com.chen.sfs.service;

import com.chen.sfs.service.common.Page;
import com.chen.sfs.service.dto.FileInfo;

public interface FilesService {

	Page<FileInfo> findFiles(String uploader, String name, Integer page, Integer size);

}
