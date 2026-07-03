package com.chen.sfs.exception;

public class FileUploadException extends AppException {

        public FileUploadException(String message) {
                super(message);
        }

        public FileUploadException(String message, Throwable cause) {
                super(message, cause);
        }

}
