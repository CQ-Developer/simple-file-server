package com.chen.sfs.service.common;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Page<T> {

	private Integer pages;
	private List<T> records;

}
