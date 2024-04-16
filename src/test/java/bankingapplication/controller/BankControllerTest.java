package bankingapplication.controller;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.model.dto.BankDto;
import bankingapplication.model.entity.Bank;
import bankingapplication.service.BankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
class BankControllerTest {

    @InjectMocks
    BankController bankController;

    @Mock
    BankService bankService;

    @Test
    void saveBank() {
        BankDto bankDto=new BankDto();
        bankDto.setBankName("NikhilBank");
        bankDto.setCity("SaiNagar");
        bankDto.setAddress("Nandurbar");
        bankDto.setIfscCode ("Abcf1234567");
        bankDto.setBranchName("Segaon");
        Mockito.when (bankService.addBank(bankDto)).thenReturn(ApplicationConstant.BANK_CREATED);
        ResponseEntity<String> stringResponseEntity = bankController.saveBank (bankDto);
        assertEquals(ApplicationConstant. BANK_CREATED, stringResponseEntity.getBody());
    }

    @Test
    void getBankById() {
        List<Bank> bankList=new ArrayList<>();
        Mockito.when(bankService.getBankByName(  "Nikhil")).thenReturn(bankList);
        ResponseEntity<List<Bank>> bankById = bankController.getBankByName(  "Nihkil");
        assertEquals(bankList, bankById.getBody());

    }

    @Test
    void updateBank() {
        BankDto bankDto=new BankDto();
        bankDto.setBankName("NikhilBank");
        bankDto.setCity("SaiNagar");
        bankDto.setAddress("Nandurbar");
        bankDto.setIfscCode ("Abcf1234567");
        bankDto.setBranchName("Segaon");
        Mockito.when(bankService.updateBank (bankDto,  1L)).thenReturn(ApplicationConstant.BANK_UPDATE);
        ResponseEntity<String> stringResponseEntity= bankController.updateBank (bankDto,  1L);
        assertEquals(ApplicationConstant.BANK_UPDATE, stringResponseEntity.getBody());

    }

    @Test
    void deleteBank() {
        Mockito.when(bankService.deleteBank (  1L)).thenReturn(ApplicationConstant.BANK_DELETED);
        ResponseEntity<String> stringResponseEntity =bankController.deleteBank(  1L);
        assertEquals(ApplicationConstant. BANK_DELETED, stringResponseEntity.getBody());
    }

    @Test
    void getAllBank() {
        List<Bank> bankList=new ArrayList<>();
        Mockito.when(bankService.getAllBank()).thenReturn(bankList);
        ResponseEntity<List<Bank>> allBank = bankController.getAllBank();
        assertEquals(bankList, allBank.getBody());
    }
}