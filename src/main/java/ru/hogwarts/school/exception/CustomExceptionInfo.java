package ru.hogwarts.school.exception;

public class CustomExceptionInfo {
    public String message;
    public String error;
    public String status;
    public String path;

    public CustomExceptionInfo() {
    }

    public CustomExceptionInfo(String message, String error, String status, String path) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
