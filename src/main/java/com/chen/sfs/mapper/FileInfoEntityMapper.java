package com.chen.sfs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.chen.sfs.repository.jpa.entity.FilesEntity;
import com.chen.sfs.service.dto.FileDownload;
import com.chen.sfs.service.dto.FileInfo;

@Mapper
public interface FileInfoEntityMapper {

        FileInfoEntityMapper INSTANCE = Mappers.getMapper(FileInfoEntityMapper.class);

        FileDownload toFileDownload(FilesEntity entity);

	FileInfo toFileInfo(FilesEntity entity);

}
