package bankingapplication.controller;

import bankingapplication.constant.UrlConstant;
import bankingapplication.model.dto.MoneyTransferDto;
import bankingapplication.model.entity.Transaction;
import bankingapplication.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping(UrlConstant.TRANSFER_MONEY)
    public ResponseEntity<String> transferMoney(@Valid @RequestBody MoneyTransferDto transactionDto) {
        return new ResponseEntity<>(transactionService.transferMoney(transactionDto), HttpStatus.CREATED);
    }

    @GetMapping(UrlConstant.ACCOUNT_TRANSACTION_BY_ID)
    public ResponseEntity<List<Transaction>> transaction(@PathVariable String accountNumberFrom) {
        return new ResponseEntity<>(transactionService.transaction(accountNumberFrom), HttpStatus.OK);
    }

    @GetMapping(UrlConstant.TRANSACTION_BY_DAYS)
    public ResponseEntity<List<Transaction>> transactionByDays(@PathVariable Long numberOfDays) {
        return new ResponseEntity<>(transactionService.transactionByDays(numberOfDays), HttpStatus.OK);
    }

}
