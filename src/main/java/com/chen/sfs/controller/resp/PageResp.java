package com.chen.sfs.controller.resp;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResp <T> {

	private Integer pages;
	private List<T> records;

}
