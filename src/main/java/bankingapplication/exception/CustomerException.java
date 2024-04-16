package bankingapplication.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class CustomerException extends RuntimeException{
    private final HttpStatus httpStatus;

    public CustomerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
