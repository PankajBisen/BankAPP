package bankingapplication.service.impl;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.exception.TransactionException;
import bankingapplication.model.dto.MoneyTransferDto;
import bankingapplication.model.entity.Account;
import bankingapplication.model.entity.Transaction;
import bankingapplication.model.enumType.SavingOrCurrentBalance;
import bankingapplication.repo.AccountRepo;
import bankingapplication.repo.TransactionRepo;
import bankingapplication.service.TransactionService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;

    public TransactionServiceImpl(TransactionRepo transactionRepo, AccountRepo accountRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }

    @Override
    public String transferMoney(MoneyTransferDto transferDto) {

        Transaction transaction = new Transaction();
        Account fromAccount = accountRepo.findByAccNo(transferDto.getAccountNumberFrom())
                .orElseThrow(()->new TransactionException("From "+ApplicationConstant.ACCOUNT_NOT_FOUND,HttpStatus.NOT_FOUND));
        Account toAccount = accountRepo.findByAccNo(transferDto.getAccountNumberTo())
                .orElseThrow(()->new TransactionException("To "+ApplicationConstant.ACCOUNT_NOT_FOUND,HttpStatus.NOT_FOUND));

        moneyTransferValidation(transferDto, fromAccount, toAccount);
        double fromAccountDebited = fromAccount.getAmount() - transferDto.getAmount();
        double toAccountCredited = toAccount.getAmount() + transferDto.getAmount();

        fromAccount.setAmount(fromAccountDebited);
        toAccount.setAmount(toAccountCredited);

        BeanUtils.copyProperties(transferDto, transaction);

        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);
        transactionRepo.save(transaction);

        saveAccountTypeTransaction(transferDto, fromAccountDebited, transaction);
        return ApplicationConstant.TRANSACTION_SUCCESSFUL;
    }

    private static void moneyTransferValidation(MoneyTransferDto transferDto, Account fromAccount, Account toAccount) {
        if (fromAccount.isBlocked()) {
            throw new TransactionException((String.format("Account no is blocked %s  ", fromAccount.getAccNo())),HttpStatus.BAD_REQUEST);
        }
        if (!transferDto.getIfscCode().equals(toAccount.getIfscCode())) {
            throw new TransactionException(ApplicationConstant.INVALID_IFSC_CODE + ApplicationConstant.DOES_NOT_MATCH + transferDto.getIfscCode(),HttpStatus.BAD_REQUEST);
        }
        if (!transferDto.getAccountType().equals(toAccount.getAccountType())) {
            throw new TransactionException(ApplicationConstant.INVALID_ACCOUNT_TYPE + ApplicationConstant.DOES_NOT_MATCH + transferDto.getAccountType(),HttpStatus.BAD_REQUEST);
        }
        if (transferDto.getAmount() > fromAccount.getAmount()) {
            throw new TransactionException(ApplicationConstant.ACCOUNT_BALANCE_LOW,HttpStatus.BAD_REQUEST);
        }
    }

    private void saveAccountTypeTransaction(MoneyTransferDto transferDto, double fromAccountDebited, Transaction transaction) {
        Account fromAccount1 = accountRepo.findByAccNo(transferDto.getAccountNumberFrom()).get();
        if (Objects.nonNull(fromAccount1)) {
            saveTransaction(fromAccount1, SavingOrCurrentBalance.SAVING,
                    fromAccountDebited < (SavingOrCurrentBalance.SAVING.getAmount()),
                    true, transaction, ApplicationConstant.TRANSACTION_SUCCESSFUL_BUT_ACCOUNT_BLOCKED,
                    SavingOrCurrentBalance.CURRENT,
                    fromAccountDebited < (SavingOrCurrentBalance.CURRENT.getAmount()),
                    ApplicationConstant.TRANSACTION_SUCCESSFUL_BUT_ACCOUNT_BLOCKED);
        }

        Account toAccount1 = accountRepo.findByAccNo(transferDto.getAccountNumberTo()).get();
        if (Objects.nonNull(toAccount1)) {
            saveTransaction(toAccount1, SavingOrCurrentBalance.CURRENT,
                    fromAccountDebited > (SavingOrCurrentBalance.CURRENT.getAmount()),
                    false, transaction, ApplicationConstant.TRANSACTION_SUCCESSFUL,
                    SavingOrCurrentBalance.SAVING,
                    fromAccountDebited > (SavingOrCurrentBalance.SAVING.getAmount()),
                    ApplicationConstant.TRANSACTION_SUCCESSFUL);
        }
    }

    private void saveTransaction(Account fromAccount1, SavingOrCurrentBalance saving,
                                   boolean fromAccountDebited, boolean isBlocked, Transaction transaction,
                                   String savingAcc, SavingOrCurrentBalance current, boolean fromAccountDebited1,
                                   String currentAcc) {
        if ((fromAccount1.getAccountType().name().equals(saving.name())) && fromAccountDebited) {
            fromAccount1.setBlocked(isBlocked);
            accountRepo.save(fromAccount1);
            transactionRepo.save(transaction);
        }
        if ((fromAccount1.getAccountType().name().equals(current.name())) && fromAccountDebited1) {
            fromAccount1.setBlocked(isBlocked);
            accountRepo.save(fromAccount1);
            transactionRepo.save(transaction);
        }
    }

    @Override
    public List<Transaction> transaction(String accountNumberFrom) {
        accountRepo.findByAccNo(accountNumberFrom).orElseThrow(()->new TransactionException(ApplicationConstant.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND));
        List<Transaction> transactions = transactionRepo.findByAccountNumberFrom(accountNumberFrom);
        if (CollectionUtils.isEmpty(transactions)) {
            throw new TransactionException(ApplicationConstant.NO_TRANSACTION, HttpStatus.BAD_REQUEST);
        } else return transactions;
    }

    @Override
    public List<Transaction> transactionByDays(Long numberOfDays) {
        List<Transaction> transactions = transactionRepo.findByDateBetween(LocalDate.now().minusDays(numberOfDays), LocalDate.now());
        if (CollectionUtils.isEmpty(transactions)) {
            throw new TransactionException(ApplicationConstant.NO_TRANSACTION_IN_BETWEEN_DAYS, HttpStatus.NOT_FOUND);
        } else return transactions;
    }
}
