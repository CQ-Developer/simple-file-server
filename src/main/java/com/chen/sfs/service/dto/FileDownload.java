package com.chen.sfs.service.dto;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDownload {

        private String name;
        private String mediaType;
        private Long size;
        private Path absolutePath;

}
