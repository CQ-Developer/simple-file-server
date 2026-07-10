package com.chen.sfs.exception;

import com.chen.sfs.controller.resp.AppResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AppException.class)
	public AppResp<Void> handleAppException(AppException e) {
		log.error(e.getMessage(), e);
		return AppResp.err(e.getMessage());
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public AppResp<Void> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
		var msg = "Missing form data";
		log.error(msg, e);
		return AppResp.err(msg);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public AppResp<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		var msg = "Unsupported request method";
		log.error(msg, e);
		return AppResp.err(msg);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public AppResp<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		var msg = "Request parameters error";
		log.error(msg, e);
		return AppResp.err(msg);
	}

}
