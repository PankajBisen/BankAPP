package bankingapplication.service;

import bankingapplication.model.dto.BankDto;
import bankingapplication.model.entity.Bank;
import java.util.List;

public interface BankService {

  String addBank(BankDto bankDto);

  List<Bank> getBankByName(String content);

  String updateBank(BankDto bankDto, Long bankId);

  String deleteBank(Long bankId);

  List<Bank> getAllBank();
}


