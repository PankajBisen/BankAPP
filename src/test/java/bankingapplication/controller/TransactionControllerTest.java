package bankingapplication.controller;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.model.dto.MoneyTransferDto;
import bankingapplication.model.entity.Transaction;
import bankingapplication.model.enumType.SavingOrCurrentBalance;
import bankingapplication.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(SpringExtension.class)
class TransactionControllerTest {

    @Mock
    TransactionService transactionService;

    @InjectMocks
    TransactionController transactionController;

    @Test
    void transferMoney() {
        MoneyTransferDto moneyTransferDto = new MoneyTransferDto();
        moneyTransferDto.setDate(LocalDate.now());
        moneyTransferDto.setName("PANKAJ");
        moneyTransferDto.setAmount(10000.00);
        moneyTransferDto.setAccountType(SavingOrCurrentBalance.CURRENT);
        moneyTransferDto.setIfscCode("FMIPB5288J");
        moneyTransferDto.setAccountNumberFrom("123456789098");
        moneyTransferDto.setAccountNumberTo("098765432123");
        Mockito.when(transactionService.transferMoney(moneyTransferDto)).thenReturn(ApplicationConstant.TRANSACTION_SUCCESSFUL);
        ResponseEntity<String> transferMoney = transactionController.transferMoney(moneyTransferDto);
        assertEquals(ApplicationConstant.TRANSACTION_SUCCESSFUL,transferMoney.getBody());
    }

    @Test
    void transaction() {
        List<Transaction> transaction = new ArrayList<>();
        Mockito.when(transactionService.transaction(ArgumentMatchers.anyString())).thenReturn(transaction);
        ResponseEntity<List<Transaction>> transaction1 = transactionController.transaction("123456789098");
        assertEquals(HttpStatus.OK.value(),transaction1.getStatusCode().value());
    }

    @Test
    void transactionByDays() {
        List<Transaction> transaction = new ArrayList<>();
        Mockito.when(transactionService.transactionByDays(ArgumentMatchers.anyLong())).thenReturn(transaction);
        ResponseEntity<List<Transaction>> transactions = transactionController.transactionByDays(7L);
        assertEquals(HttpStatus.OK.value(),transactions.getStatusCode().value());
    }
}