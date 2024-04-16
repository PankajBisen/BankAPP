package bankingapplication.service;

import bankingapplication.model.dto.MoneyTransferDto;
import bankingapplication.model.entity.Transaction;
import java.util.List;

public interface TransactionService {

  String transferMoney(MoneyTransferDto transactionDto);

  List<Transaction> transaction(String accountNumberFrom);

  List<Transaction> transactionByDays(Long numberOfDays);
}
