package com.chen.sfs.controller.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppResp<T> {

        private boolean success;
        private String message;
        private T data;

        public static <T> AppResp<T> ok(T data) {
                return new AppResp<>(true, "ok", data);
        }

        public static AppResp<Void> ok() {
                return ok(null);
        }

        public static AppResp<Void> err(String msg) {
                return new AppResp<>(false, msg, null);
        }

        public static AppResp<Void> err() {
                return err("未知错误");
        }

}
