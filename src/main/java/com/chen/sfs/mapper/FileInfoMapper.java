package com.chen.sfs.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.chen.sfs.controller.resp.FileInfoResp;
import com.chen.sfs.service.dto.FileInfo;

@Mapper
public interface FileInfoMapper {

	FileInfoMapper INSTANCE = Mappers.getMapper(FileInfoMapper.class);

	List<FileInfoResp> toFileInfoResps(List<FileInfo> files);

}
