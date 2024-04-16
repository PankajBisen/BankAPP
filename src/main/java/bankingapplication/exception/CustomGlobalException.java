package bankingapplication.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomGlobalException {

    private static ResponseEntity<Map<String, Object>> prepareExceptionResponse(HttpStatus httpStatus, Object message) {
        Map<String, Object> exceptionBody = new HashMap<>();
        exceptionBody.put("timestamp", new Date());
        exceptionBody.put("status", httpStatus.value());
        exceptionBody.put("message", message);
        return new ResponseEntity<>(exceptionBody, httpStatus);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        return prepareExceptionResponse(HttpStatus.BAD_REQUEST, methodArgumentNotValidException.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));

    }

    @ExceptionHandler(value = {BankException.class})
    public ResponseEntity<?> bankException(BankException bankException) {
        return prepareExceptionResponse(bankException.getHttpStatus(), bankException.getMessage());
    }

    @ExceptionHandler(value = {CustomerException.class})
    public ResponseEntity<?> customerException(CustomerException exception) {
        return prepareExceptionResponse(exception.getHttpStatus(), exception.getMessage());
    }

    @ExceptionHandler(value = {AccountException.class})
    public ResponseEntity<?> accountException(AccountException exception) {
        return prepareExceptionResponse(exception.getHttpStatus(), exception.getMessage());
    }

    @ExceptionHandler(value = {TransactionException.class})
    public ResponseEntity<?> transactionException(TransactionException exception) {
        return prepareExceptionResponse(exception.getHttpStatus(), exception.getMessage());
    }
}
