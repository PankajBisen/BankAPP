package bankingapplication.service.impl;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.exception.AccountException;
import bankingapplication.model.dto.AccountDto;
import bankingapplication.model.entity.Account;
import bankingapplication.model.entity.Bank;
import bankingapplication.model.enumType.SavingOrCurrentBalance;
import bankingapplication.repo.AccountRepo;
import bankingapplication.repo.BankRepo;
import bankingapplication.repo.CustomerRepo;
import bankingapplication.repo.TransactionRepo;
import bankingapplication.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final BankRepo bankRepo;
    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;

    public AccountServiceImpl(BankRepo bankRepo, AccountRepo accountRepo, CustomerRepo customerRepo, TransactionRepo transactionRepo) {
        this.bankRepo = bankRepo;
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public String saveAccountNo(AccountDto accountDto) {
        Account account = dtoToEntity(accountDto);
        List<Account> byCustomerAndBankAndAccountType = accountRepo.findByCustomerAndBankAndAccountType(
                account.getCustomer(), account.getBank(), account.getAccountType());
        if(Objects.nonNull(byCustomerAndBankAndAccountType)){
            accountTypeBalance(byCustomerAndBankAndAccountType, account);
        }
        generateAccountNo(account);
        if (Objects.nonNull(accountRepo.save(account))) {
            return ApplicationConstant.ACCOUNT_CREATE;
        }else{
            throw new AccountException(ApplicationConstant.ERROR_OCCURRED_WHILE_SAVING_INTO_THE_DATA_BASE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void generateAccountNo(Account account) {
        String accountNumber;
        Account byAccNo;
        do {
            accountNumber = RandomStringUtils.random(12, false, true);
            byAccNo = accountRepo.findByAccNo(accountNumber).orElse(null);
        } while (Objects.nonNull(byAccNo));
        account.setAccNo(accountNumber);
    }

    private static void accountTypeBalance(List<Account> byCustomerAndBankAndAccountType, Account account) {
        if (!byCustomerAndBankAndAccountType.isEmpty()) {
            throw new AccountException(String.format("Customer already have %s account", account.getAccountType()), HttpStatus.BAD_REQUEST);
        } else if ((account.getAccountType().name().equals(SavingOrCurrentBalance.SAVING.name())) && (account.getAmount() < SavingOrCurrentBalance.SAVING.getAmount())) {
            throw new AccountException(ApplicationConstant.MINIMUM_BALANCE_FOR + " saving account 5000",HttpStatus.BAD_REQUEST);
        } else if ((account.getAccountType().name().equals(SavingOrCurrentBalance.CURRENT.name())) && (account.getAmount() < SavingOrCurrentBalance.CURRENT.getAmount())) {
            throw new AccountException(ApplicationConstant.MINIMUM_BALANCE_FOR + " current account 10000", HttpStatus.BAD_REQUEST);
        }
    }

    private Account dtoToEntity(AccountDto accountDto) {
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        account.setAccountType(accountDto.getAccountType());
        account.setBank(bankRepo.findById(accountDto.getBankId()).orElseThrow(()->
                new AccountException("The bank " + ApplicationConstant.ID_INVALID + accountDto.getBankId(),
                                HttpStatus.BAD_REQUEST)));
        account.setCustomer(customerRepo.findById(accountDto.getCustomerId()).orElseThrow(()->
                new AccountException("The customer " + ApplicationConstant.ID_INVALID + accountDto.getCustomerId(),
                        HttpStatus.BAD_REQUEST)));
        return account;
    }

    public List<AccountDto> accountByAccNo(String content) {
        List<Account> accounts = accountRepo.findByTitleContent("%" + content + "%");
        if (accounts.isEmpty()) {
            throw new AccountException(ApplicationConstant.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);
        } else {
            return accounts.stream().map(this::entityToDto).collect(Collectors.toList());
        }
    }

    @Override
    public List<AccountDto> getAllAccount() {
        List<Account> accounts = accountRepo.findAll();
        if (accounts.isEmpty()) {
            throw new AccountException(ApplicationConstant.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);
        } else {
            return accounts.stream().map(this::entityToDto).collect(Collectors.toList());
        }
    }

    @Override
    public String updateAccount(AccountDto accountDto, Long accountId) {
        accountRepo.findById(accountId).ifPresentOrElse((e) -> {
            e.setAccountType(accountDto.getAccountType());
            e.setName(accountDto.getName());
            accountRepo.save(e);
        }, () -> {
            throw new AccountException(ApplicationConstant.ACCOUNT_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        });
        return ApplicationConstant.ACCOUNT_UPDATED;
    }

    @Override
    public String deleteAccount(Long accountId) {
        accountRepo.findById(accountId).ifPresentOrElse((e) -> accountRepo.deleteById(accountId), () -> {
            throw new AccountException(ApplicationConstant.ACCOUNT_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        });
        return ApplicationConstant.ACCOUNT_DELETED;
    }

    @Override
    public List<AccountDto> getAllByBankId(Long bankId) {
        Bank bank = bankRepo.findById(bankId).orElseThrow(() -> new AccountException(ApplicationConstant.BANK_IS_NOT_FOUND, HttpStatus.BAD_REQUEST));
        List<Account> accounts = accountRepo.findByBank(bank);
        if (accounts.isEmpty()) {
            throw new AccountException(ApplicationConstant.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return accounts.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    private AccountDto entityToDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setBankId(account.getBank().getBankId());
        accountDto.setCustomerId(account.getCustomer().getCustomerId());
        BeanUtils.copyProperties(account, accountDto);
        return accountDto;
    }
}
