package bankingapplication.controller;

import bankingapplication.constant.UrlConstant;
import bankingapplication.model.dto.BankDto;
import bankingapplication.model.entity.Bank;
import bankingapplication.service.BankService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConstant.BANK)
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping(UrlConstant.CREATE_BANK)
    public ResponseEntity<String> saveBank(@Valid @RequestBody BankDto bankDto) {
        return new ResponseEntity<>(bankService.addBank(bankDto), HttpStatus.CREATED);
    }


    @GetMapping(UrlConstant.GET_BANK)
    public ResponseEntity<List<Bank>> getBankByName(@PathVariable String content) {
        return new ResponseEntity<>(bankService.getBankByName(content), HttpStatus.OK);
    }


    @PutMapping(UrlConstant.BANK_UPDATE)
    public ResponseEntity<String> updateBank(@Valid @RequestBody BankDto bankDto, @PathVariable Long bankId) {
        return new ResponseEntity<>(bankService.updateBank(bankDto, bankId), HttpStatus.OK);
    }


    @DeleteMapping(UrlConstant.BANK_DELETE)
    public ResponseEntity<String> deleteBank(@PathVariable Long bankId) {
        return new ResponseEntity<>(bankService.deleteBank(bankId), HttpStatus.OK);
    }

    @GetMapping(UrlConstant.GET_ALL_BANK)
    public ResponseEntity<List<Bank>> getAllBank() {
        return new ResponseEntity<>(bankService.getAllBank(), HttpStatus.OK);
    }

}
