package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CustomExceptionInfo> handleException404(CustomNotFoundException ex, HttpServletRequest request) {
        CustomExceptionInfo handle = new CustomExceptionInfo();
        HttpStatus status = HttpStatus.NOT_FOUND;
        handle.setMessage(ex.getMessage());
        handle.setError(status.getReasonPhrase());
        handle.setStatus(String.valueOf(status.value()));
        handle.setPath(request.getRequestURI());
        return new ResponseEntity<>(handle, status);
    }

    @ExceptionHandler
    public ResponseEntity<CustomExceptionInfo> handleException400(CustomBadRequestException ex, HttpServletRequest request) {
        CustomExceptionInfo handle = new CustomExceptionInfo();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        handle.setMessage(ex.getMessage());
        handle.setError(status.getReasonPhrase());
        handle.setStatus(String.valueOf(status.value()));
        handle.setPath(request.getRequestURI());
        return new ResponseEntity<>(handle, status);
    }
}
