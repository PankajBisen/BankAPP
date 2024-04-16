package bankingapplication.controller;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.model.dto.AccountDto;
import bankingapplication.model.enumType.SavingOrCurrentBalance;
import bankingapplication.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AccountControllerTest {
    @InjectMocks
    AccountController accountController;

    @Mock
    AccountService accountService;

    @Test
    void save() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(1L);
        accountDto.setAccountType(SavingOrCurrentBalance.CURRENT);
        accountDto.setAmount(1000d);
        accountDto.setName("ICICI");
        accountDto.setCustomerId(1L);
        accountDto.setBlocked(true);
        accountDto.setIfscCode("Aasd1234567");
        accountDto.setAccNo("123456789098");
        Mockito.when(accountService.saveAccountNo(accountDto)).thenReturn(ApplicationConstant.ACCOUNT_CREATE);
        ResponseEntity<?> save = accountController.save(accountDto);
        assertEquals(ApplicationConstant.ACCOUNT_CREATE, save.getBody());
    }

    @Test
    void getAccount() {
        List<AccountDto> accountDtoList = new ArrayList<>();
        Mockito.when(accountService.accountByAccNo("pankaj ")).thenReturn(accountDtoList);
        ResponseEntity<?> pankaj = accountController.accountByAccNo("pankaj");
        assertEquals(accountDtoList, pankaj.getBody());
    }

    @Test
    void getAllAccount() {
        List<AccountDto> accountDtoList = new ArrayList<>();
        Mockito.when(accountService.getAllAccount()).thenReturn(accountDtoList);
        ResponseEntity<List<AccountDto>> allAccount = accountController.getAllAccount();
        assertEquals(accountDtoList.size(), allAccount.getBody().size());
    }

    @Test
    void updateBank() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(1L);
        accountDto.setAccountType(SavingOrCurrentBalance.CURRENT);
        accountDto.setAmount(1000d);
        accountDto.setName("ICICI");
        accountDto.setBankId(1L);
        accountDto.setCustomerId(1L);
        accountDto.setBlocked(true);
        accountDto.setIfscCode("Aasd1234567");
        accountDto.setAccNo("123456789098");
        Mockito.when(accountService.updateAccount(accountDto, 1L)).thenReturn(ApplicationConstant.ACCOUNT_UPDATED);
        ResponseEntity<String> stringResponseEntity = accountController.updateBank(accountDto, 1L);
        assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
    }

    @Test
    void deleteAccount() {
        Mockito.when(accountService.deleteAccount(1L)).thenReturn(ApplicationConstant.ACCOUNT_DELETED);
        ResponseEntity<String> stringResponseEntity = accountController.deleteAccount(1L);
        assertEquals(ApplicationConstant.ACCOUNT_DELETED, stringResponseEntity.getBody());
    }

    @Test
    void getAllByBankId() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(1L);
        accountDto.setAccountType(SavingOrCurrentBalance.CURRENT);
        accountDto.setName("ICICI");
        accountDto.setBankId(1L);
        accountDto.setAccNo("123456789098");
        List<AccountDto> accountDtoList = new ArrayList<>();
        accountDtoList.add(accountDto);
        accountDto.setAmount(1000d);
        accountDto.setCustomerId(1L);
        accountDto.setBlocked(true);
        Mockito.when(accountService.getAllByBankId(1L)).thenReturn(accountDtoList);
        ResponseEntity<List<AccountDto>> allByBankId = accountController.getAllByBankId(1L);
        assertEquals(accountDtoList, allByBankId.getBody());
    }
}