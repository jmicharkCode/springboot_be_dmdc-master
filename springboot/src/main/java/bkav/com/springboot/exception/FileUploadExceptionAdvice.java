package bkav.com.springboot.exception;

import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileUploadExceptionAdvice {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<MessageResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(Status.EXCEPTION, Message.FILE_TOO_LARGE, false, true));
    }
}
