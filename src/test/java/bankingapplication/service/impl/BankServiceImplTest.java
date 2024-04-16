package bankingapplication.service.impl;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.exception.BankException;
import bankingapplication.model.dto.BankDto;
import bankingapplication.model.entity.Account;
import bankingapplication.model.entity.Bank;
import bankingapplication.repo.AccountRepo;
import bankingapplication.repo.BankRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class BankServiceImplTest {

    @InjectMocks
    BankServiceImpl bankService;

    @Mock
    BankRepo bankRepo;

    @Mock
    AccountRepo accountRepo;

    @Test
    void addBank() {
        Bank bank = new Bank();
        bank.setIfscCode("BBCN7755552");
        BankDto bankDto = new BankDto();
        bankDto.setBankName("ICICI");
        bankDto.setIfscCode("ABCN7755551");
        bankDto.setCity("Nagpur");
        bankDto.setAddress("SAINAgar");
        bankDto.setBranchName("BhavaniMandir");
        Mockito.when(bankRepo.findByIfscCode(bankDto.getIfscCode())).thenReturn(Optional.empty());
        Mockito.when(bankRepo.save(bank)).thenReturn(bank);
        assertEquals(ApplicationConstant.BANK_CREATED, bankService.addBank(bankDto));

        bankDto.setIfscCode("Aesv123456");
        BankException bankException = assertThrows(BankException.class, () -> bankService.addBank(bankDto));
        assertEquals(HttpStatus.BAD_REQUEST, bankException.getHttpStatus());

        Bank bank1 = new Bank();
        BankDto bankDto1 = new BankDto();
        bankDto1.setBankName("ICICI");
        bankDto1.setIfscCode("FMIPB5288J");
        bankDto1.setCity("Nagpur");
        bankDto1.setAddress("SAINAgar");
        bankDto1.setBranchName("BhavaniMandir");
        Mockito.when(bankRepo.findByIfscCode(bankDto1.getIfscCode())).thenReturn(Optional.ofNullable(bank1));
        BankException bankExcep = assertThrows(BankException.class, () -> bankService.addBank(bankDto));
        assertEquals(HttpStatus.BAD_REQUEST, bankExcep.getHttpStatus());
    }

    @Test
    void getBankById() {
        Bank bank = new Bank();
        List<Bank> bankList = new ArrayList<>();
        bankList.add(bank);
        Mockito.when(bankRepo.findByTitleContent(Mockito.anyString())).thenReturn(Optional.of(bankList));
        assertEquals(bankList.size(), bankService.getBankByName("Pankaj").size());

        List<Bank> bankList1 = new ArrayList<>();
        Mockito.when(bankRepo.findByTitleContent(Mockito.anyString())).thenReturn(Optional.of(bankList1));
        assertEquals(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @Test
    void updateBank() {
        Bank bank = new Bank();

        BankDto bankDto = new BankDto();
        bankDto.setBankName("ICICI");
        bankDto.setIfscCode("FMIPB7899P");
        bankDto.setCity("Nagpur");
        bankDto.setAddress("SAINAgar");
        bankDto.setBranchName("BhavaniMandir");

        Mockito.when(bankRepo.findById(1L)).thenReturn(Optional.of(bank));
        Mockito.when(bankRepo.findByIfscCode(bankDto.getIfscCode())).thenReturn(Optional.empty());
        Mockito.when(bankRepo.save(bank)).thenReturn(bank);
        assertEquals(ApplicationConstant.BANK_UPDATE, bankService.updateBank(bankDto, 1L));

        Mockito.when(bankRepo.findById(1L)).thenReturn(Optional.ofNullable(null));
        BankException bankException = assertThrows(BankException.class, () -> bankService.updateBank(bankDto, 1L));
        assertEquals(ApplicationConstant.BANK_IS_NOT_FOUND, bankException.getMessage());

        Mockito.when(bankRepo.findByIfscCode(bankDto.getIfscCode())).thenReturn(Optional.of(bank));
        Mockito.when(bankRepo.findById(1L)).thenReturn(Optional.of(bank));
        BankException bankExcep = assertThrows(BankException.class, () -> bankService.updateBank(bankDto, 1L));
        assertEquals(ApplicationConstant.BANK_ALREADY_REGISTER_FOR_THIS_IFSC_CODE, bankExcep.getMessage());
    }

    @Test
    void deleteBank() {
        Bank bank = new Bank();
        List<Account> accountList = new ArrayList<>();
        Account account = new Account();
        Mockito.when(bankRepo.findById(1L)).thenReturn(Optional.of(bank));
        Mockito.when(accountRepo.findByBank(bank)).thenReturn(accountList);
        assertEquals(ApplicationConstant.BANK_DELETED, bankService.deleteBank(1L));

        Mockito.when(bankRepo.findById(1L)).thenReturn(Optional.ofNullable(null));
        BankException bankException = assertThrows(BankException.class, () -> bankService.deleteBank(1L));
        assertEquals(HttpStatus.NOT_FOUND, bankException.getHttpStatus());
        accountList.add(account);
        Mockito.when(bankRepo.findById(1L)).thenReturn(Optional.of(bank));
        Mockito.when(accountRepo.findByBank(bank)).thenReturn(accountList);
        BankException bankException1 = assertThrows(BankException.class, () -> bankService.deleteBank(1L));
        assertEquals(HttpStatus.BAD_REQUEST, bankException1.getHttpStatus());
    }

    @Test
    void getAllBank() {
        Bank bank = new Bank();
        List<Bank> bankList = new ArrayList<>();
        bankList.add(bank);
        Mockito.when(bankRepo.findAll()).thenReturn(bankList);
        assertEquals(bankList, bankService.getAllBank());
        List<Bank> bankList1 = new ArrayList<>();
        Mockito.when(bankRepo.findAll()).thenReturn(bankList1);
        BankException bankException = assertThrows(BankException.class, () -> bankService.getAllBank());
        assertEquals(HttpStatus.BAD_REQUEST, bankException.getHttpStatus());
    }
}