package bankingapplication.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BankException extends RuntimeException {
    private final HttpStatus httpStatus;

    public BankException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
