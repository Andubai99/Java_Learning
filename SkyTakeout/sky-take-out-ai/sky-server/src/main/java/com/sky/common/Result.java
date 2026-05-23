package com.sky.common;

public record Result<T>(int code, String msg, T data) {
    public static <T> Result<T> success(T data) {
        return new Result<>(1, null, data);
    }

    public static Result<Void> success() {
        return new Result<>(1, null, null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(0, msg, null);
    }
}
