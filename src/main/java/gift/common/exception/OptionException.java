package gift.common.exception;

import org.springframework.http.HttpStatus;

public class OptionException extends RuntimeException{

    private final HttpStatus httpStatus;

    public OptionException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();

    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}