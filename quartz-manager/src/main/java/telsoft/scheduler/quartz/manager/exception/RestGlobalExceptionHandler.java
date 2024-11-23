package telsoft.scheduler.quartz.manager.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

@RestControllerAdvice
@Log4j2
public class RestGlobalExceptionHandler {

    public RestGlobalExceptionHandler(HttpServletRequest request) {
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<?> handler(IOException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IO exception: " + e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<?> handler(ValidationException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation exception: " + e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<?> handler(NotFoundException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found exception: " + e.getMessage());
    }

    @ExceptionHandler({ClassNotFoundException.class})
    public ResponseEntity<?> handler(ClassNotFoundException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Class not found exception: " + e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handler(AccessDeniedException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied exception: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnwantedException(Exception e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unwanted exception: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handler(RuntimeException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime exception: " + e.getMessage());
    }

    @ExceptionHandler({RestClientException.class})
    public ResponseEntity<?> handler(RestClientException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rest client exception: " + e.getMessage());
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> handler(NullPointerException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Null pointer exception: " + e.getMessage());
    }

    @ExceptionHandler({NoSuchFileException.class})
    public ResponseEntity<?> handler(NoSuchFileException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No such file exception: " + e.getMessage());
    }

    @ExceptionHandler({SchedulerException.class})
    public ResponseEntity<?> handler(SchedulerException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Scheduler exception: " + e.getMessage());
    }


    private void logErrorDetail(Exception e) {
        log.error("Error: {}\nStackTrace: {}", e.getMessage(), e.getStackTrace());
    }
}
