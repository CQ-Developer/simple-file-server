package com.chen.sfs.service;

import java.util.UUID;

import com.chen.sfs.service.dto.FileDownload;

public interface FileDownloadService {

        FileDownload download(UUID id);

}
