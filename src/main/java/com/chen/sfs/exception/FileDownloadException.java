package com.chen.sfs.exception;

public class FileDownloadException extends AppException {

        public FileDownloadException(String message) {
                super(message);
        }

        public FileDownloadException(String message, Throwable cause) {
                super(message, cause);
        }

}
