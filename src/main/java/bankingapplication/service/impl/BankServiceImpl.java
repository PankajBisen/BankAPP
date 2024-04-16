package bankingapplication.service.impl;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.exception.BankException;
import bankingapplication.model.dto.BankDto;
import bankingapplication.model.entity.Account;
import bankingapplication.model.entity.Bank;
import bankingapplication.repo.AccountRepo;
import bankingapplication.repo.BankRepo;
import bankingapplication.service.BankService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    private final BankRepo bankRepo;

    private final AccountRepo accountRepo;

    public BankServiceImpl(BankRepo bankRepo, AccountRepo accountRepo) {
        this.bankRepo = bankRepo;
        this.accountRepo = accountRepo;
    }

    @Override
    public String addBank(BankDto bankDto) {
        if (bankDto.getIfscCode().length() != 11) {
            throw new BankException(ApplicationConstant.INVALID_IFSC_CODE, HttpStatus.BAD_REQUEST);
        }
        bankRepo.findByIfscCode(bankDto.getIfscCode()).ifPresentOrElse((e) -> {
            throw new BankException(ApplicationConstant.BANK_ALREADY_REGISTER_FOR_THIS_IFSC_CODE, HttpStatus.NOT_FOUND);
        }, () -> {
            bankRepo.save(dtoToEntity(bankDto));
        });
        return ApplicationConstant.BANK_CREATED;
    }

    @Override
    public List<Bank> getBankByName(String content) {
        return bankRepo.findByTitleContent("%" + content + "%").orElseThrow(() ->
                      new BankException(ApplicationConstant.BANK_IS_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public String updateBank(BankDto bankDto, Long bankId) {
        bankRepo.findByIfscCode(bankDto.getIfscCode()).ifPresent((e) -> {
            throw new BankException(ApplicationConstant.BANK_ALREADY_REGISTER_FOR_THIS_IFSC_CODE, HttpStatus.CONFLICT);
        });
        bankRepo.findById(bankId).ifPresentOrElse((bank) -> {
            BeanUtils.copyProperties(bankDto,bank);
            bankRepo.save(bank);
        }, () -> {
            throw new BankException(ApplicationConstant.BANK_IS_NOT_FOUND, HttpStatus.BAD_REQUEST);
        });
        return ApplicationConstant.BANK_UPDATE;
    }

    @Override
    public String deleteBank(Long bankId) {
        bankRepo.findById(bankId).ifPresentOrElse((e) -> {
            List<Account> accounts = accountRepo.findByBank(e);
            if (!accounts.isEmpty()) {
                throw new BankException(ApplicationConstant.CANT_DELETE_BANK_BECAUSE_ACCOUNT_PRESENT, HttpStatus.BAD_REQUEST);
            } else {
                bankRepo.deleteById(bankId);
            }
        }, () -> {
            throw new BankException(ApplicationConstant.BANK_IS_NOT_FOUND, HttpStatus.NOT_FOUND);
        });
        return ApplicationConstant.BANK_DELETED;
    }

    @Override
    public List<Bank> getAllBank() {
        List<Bank> banks = bankRepo.findAll();
        if (banks.isEmpty()) {
            throw new BankException(ApplicationConstant.BANK_IS_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        return banks;
    }

    private Bank dtoToEntity(BankDto bankDto) {
        Bank bank = new Bank();
        BeanUtils.copyProperties(bankDto, bank);
        return bank;
    }
}
