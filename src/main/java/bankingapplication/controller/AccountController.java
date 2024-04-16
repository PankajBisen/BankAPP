package bankingapplication.controller;

import bankingapplication.constant.UrlConstant;
import bankingapplication.model.dto.AccountDto;
import bankingapplication.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(UrlConstant.ACCOUNT)
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(UrlConstant.ACCOUNT_CREAT)
    public ResponseEntity<String> save(@Valid @RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.saveAccountNo(accountDto), HttpStatus.CREATED);
    }

    @GetMapping(UrlConstant.GET_ACCOUNT)
    public ResponseEntity<List<AccountDto>> accountByAccNo(@PathVariable String content) {
        return new ResponseEntity<>(accountService.accountByAccNo(content), HttpStatus.OK);

    }

    @GetMapping(UrlConstant.ALL_BANK_ACCOUNT)
    public ResponseEntity<List<AccountDto>> getAllAccount() {
        return new ResponseEntity<>(accountService.getAllAccount(), HttpStatus.OK);
    }

    @PutMapping(UrlConstant.UPDATE_ACCOUNT)
    public ResponseEntity<String> updateBank(@RequestBody AccountDto accountDto, @PathVariable Long accountId) {
        return new ResponseEntity<>(accountService.updateAccount(accountDto, accountId), HttpStatus.OK);
    }

    @DeleteMapping(UrlConstant.DELETE_ACCOUNT)
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        return new ResponseEntity<>(accountService.deleteAccount(accountId), HttpStatus.OK);
    }

    @GetMapping(UrlConstant.GET_ALL_BANK_BY_ID)
    public ResponseEntity<List<AccountDto>> getAllByBankId(@PathVariable Long bankId) {
        return new ResponseEntity<>(accountService.getAllByBankId(bankId), HttpStatus.OK);
    }
}

