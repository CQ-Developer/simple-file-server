package com.chen.sfs.exception;

public class DatabaseOperationException extends AppException {

        public DatabaseOperationException(String message) {
                super(message);
        }

        public DatabaseOperationException(String message, Throwable cause) {
                super(message, cause);
        }

}
