package com.chen.sfs.exception;

public class FileDeleteException extends AppException {

        public FileDeleteException(String message) {
                super(message);
        }

        public FileDeleteException(String message, Throwable cause) {
                super(message, cause);
        }

}
