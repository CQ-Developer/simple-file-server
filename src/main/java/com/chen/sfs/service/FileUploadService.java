package com.chen.sfs.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

        void upload(String uploader, List<MultipartFile> files);

}
