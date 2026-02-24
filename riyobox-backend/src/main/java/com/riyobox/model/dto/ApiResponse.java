package com.riyobox.model.dto;

import java.util.Map;

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private Map<String, String> errors;
    private long timestamp = System.currentTimeMillis();

    public ApiResponse() {}

    public static <T> ApiResponseBuilder<T> builder() { return new ApiResponseBuilder<>(); }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setSuccess(true);
        res.setData(data);
        return res;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> res = success(data);
        res.setMessage(message);
        return res;
    }

    public static ApiResponse<Void> error(String message) {
        ApiResponse<Void> res = new ApiResponse<>();
        res.setSuccess(false);
        res.setMessage(message);
        return res;
    }

    public static ApiResponse<Void> error(String message, Map<String, String> errors) {
        ApiResponse<Void> res = error(message);
        res.setErrors(errors);
        return res;
    }

    public static class ApiResponseBuilder<T> {
        private ApiResponse<T> res = new ApiResponse<>();
        public ApiResponseBuilder<T> success(boolean s) { res.success = s; return this; }
        public ApiResponseBuilder<T> data(T d) { res.data = d; return this; }
        public ApiResponseBuilder<T> message(String m) { res.message = m; return this; }
        public ApiResponseBuilder<T> errors(Map<String, String> e) { res.errors = e; return this; }
        public ApiResponseBuilder<T> timestamp(long t) { res.timestamp = t; return this; }
        public ApiResponse<T> build() { return res; }
    }
}
