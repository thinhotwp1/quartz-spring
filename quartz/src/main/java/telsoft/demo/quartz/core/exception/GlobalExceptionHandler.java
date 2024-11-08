package telsoft.demo.quartz.core.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    private final HttpServletRequest request;

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<?> handler(IOException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IO exception: " + e);
    }
    @ExceptionHandler({ClassNotFoundException.class})
    public ResponseEntity<?> handler(ClassNotFoundException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Class not found exception: " + e);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handler(AccessDeniedException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied exception: " + e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnwantedException(Exception e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unwanted exception: " + e);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handler(RuntimeException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime exception: " + e.getMessage());
    }

    @ExceptionHandler({RestClientException.class})
    public ResponseEntity<?> handler(RestClientException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rest client exception: " + e);
    }

    private void logErrorDetail(Exception e) {
        // log Error Detail
//        log.info("Error detail: "+ e.getMessage());
    }
}
