package bankingapplication.service.impl;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.exception.CustomerException;
import bankingapplication.model.dto.CustomerDto;
import bankingapplication.model.dto.CustomerUpdateDto;
import bankingapplication.model.entity.Account;
import bankingapplication.model.entity.Bank;
import bankingapplication.model.entity.Customer;
import bankingapplication.repo.AccountRepo;
import bankingapplication.repo.BankRepo;
import bankingapplication.repo.CustomerRepo;
import bankingapplication.repo.TransactionRepo;
import bankingapplication.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;
    private final BankRepo bankRepo;

    public CustomerServiceImpl(CustomerRepo customerRepo, AccountRepo accountRepo, TransactionRepo transactionRepo, BankRepo bankRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
        this.bankRepo = bankRepo;
    }

    public String save(CustomerDto customerDto) {
        String aadhaarNumber = customerDto.getAadhaarNumber();
        String panNumber = customerDto.getPanCardNumber();
        String mobileNumber = customerDto.getMobileNumber();
        String emailId = customerDto.getEmailId();

        List<Customer> byPanNuOrAadhaarNumOrMobileNum = customerRepo.findByPanCardNumberOrAadhaarNumber(panNumber, aadhaarNumber);
        List<Customer> byMobileNumberOrEmailId = customerRepo.findByMobileNumberOrEmailId(mobileNumber, emailId);
        Bank byId = bankRepo.findById(customerDto.getBankId()).orElseThrow(() -> new CustomerException("The bank " + ApplicationConstant.ID_INVALID + customerDto.getBankId(), HttpStatus.BAD_REQUEST));
        Optional<Customer> byAadhaarNumberAndPanCardNumberAndBank = customerRepo.findByAadhaarNumberAndPanCardNumberAndBank(aadhaarNumber, panNumber, byId);
        Customer customerFromDb = null;
        return customerValidation(customerDto, byAadhaarNumberAndPanCardNumberAndBank, customerFromDb, byPanNuOrAadhaarNumOrMobileNum, byMobileNumberOrEmailId, aadhaarNumber, panNumber, mobileNumber);
    }

    private String customerValidation(CustomerDto customerDto, 
                                      Optional<Customer> byAadhaarNumberAndPanCardNumberAndBank, 
                                      Customer customerFromDb, 
                                      List<Customer> byPanNuOrAadhaarNumOrMobileNum, 
                                      List<Customer> byMobileNumberOrEmailId, String aadhaarNumber, 
                                      String panNumber, String mobileNumber) {
        if (byAadhaarNumberAndPanCardNumberAndBank.isPresent()) {
            customerFromDb = byAadhaarNumberAndPanCardNumberAndBank.get();
        }
        if (Objects.nonNull(customerFromDb)) {
            for (Customer customer : byPanNuOrAadhaarNumOrMobileNum) {
                if (customer.getBank().equals(customerFromDb.getBank())) {
                    throw new CustomerException(String.format("You Already registered for given bank %s", customer.getBank().getBankName()), HttpStatus.BAD_REQUEST);
                }
            }
        }
        if (byPanNuOrAadhaarNumOrMobileNum != null) {
            for (Customer c : byPanNuOrAadhaarNumOrMobileNum) {
                if (c.getBank().getBankId().equals(customerDto.getBankId()) && (c.getAadhaarNumber().equals(customerDto.getAadhaarNumber()) || c.getPanCardNumber().equals(customerDto.getPanCardNumber()))) {
                    return ApplicationConstant.PAN_OR_AADHAAR_NUMBER_NOT_UNIQUE;
                }
            }
        }
        if (byMobileNumberOrEmailId != null) {
            for (Customer c : byMobileNumberOrEmailId) {
                if (c.getBank().getBankId().equals(customerDto.getBankId()) && (c.getEmailId().equals(customerDto.getEmailId()) || c.getMobileNumber().equals(customerDto.getMobileNumber()))) {
                    return ApplicationConstant.MOBILE_NUMBER_OR_EMAILID_NOT_UNIQUE;
                }
            }
        }
        if (aadhaarNumber.length() == 12 && panNumber.length() == 10 && mobileNumber.length() == 10) {
            customerRepo.save(dtoToEntityCustomer(customerDto));
            return ApplicationConstant.CUSTOMER_CREATED;
        }
        return null;
    }

    @Override
    public List<CustomerDto> getAllCustomerByBankId(Long bankId) {
        List<Customer> customers = customerRepo.checkCustomerByBankId(bankId);
        if (CollectionUtils.isEmpty(customers)) {
            throw new CustomerException(ApplicationConstant.NO_CUSTOMER_FOR_GIVEN_BANK_ID, HttpStatus.BAD_REQUEST);
        }
        return customers.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto> getAllByBankId(Long bankId) {
        Bank bank = bankRepo.findById(bankId).orElseThrow(() -> new CustomerException(ApplicationConstant.BANK_IS_NOT_FOUND, HttpStatus.BAD_REQUEST));
        List<Customer> customers = customerRepo.findByBank(bank);
        if (CollectionUtils.isEmpty(customers)) {
            throw new CustomerException(ApplicationConstant.CUSTOMER_NOT_FOUND_OR_CUSTOMER_DOESNT_EXIST, HttpStatus.NOT_FOUND);
        }
        return customers.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    private CustomerDto entityToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setCustomerName(customer.getCustomerName());
        customerDto.setAddress(customer.getAddress());
        customerDto.setAadhaarNumber(customer.getAadhaarNumber());
        customerDto.setPanCardNumber(customer.getPanCardNumber());
        customerDto.setMobileNumber(customer.getMobileNumber());
        customerDto.setEmailId(customer.getEmailId());
        customerDto.setBankId(customer.getBank().getBankId());
        return customerDto;
    }

    private Customer dtoToEntityCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setAddress(customerDto.getAddress());
        customer.setAadhaarNumber(customerDto.getAadhaarNumber());
        customer.setPanCardNumber(customerDto.getPanCardNumber());
        customer.setMobileNumber(customerDto.getMobileNumber());
        customer.setEmailId(customerDto.getEmailId());
        customer.setPassword(customerDto.getPassword());
        bankRepo.findById(customerDto.getBankId()).ifPresentOrElse((e) -> {
            customer.setBank(e);
        }, () -> {
            throw new CustomerException("The bank " + ApplicationConstant.ID_INVALID + customerDto.getBankId(), HttpStatus.BAD_REQUEST);
        });
        return customer;
    }

    @Override
    public List<CustomerDto> getByEmailOrName(String content) {
        List<Customer> customer = customerRepo.findByTitleContent("%" + content + "%");
        if (customer.isEmpty()) {
            throw new CustomerException(ApplicationConstant.CUSTOMER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return customer.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto> getAllCustomer() {
        List<Customer> customers = customerRepo.findAll();
        if (customers.isEmpty()) {
            throw new CustomerException(ApplicationConstant.CUSTOMER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        return customers.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public String updateCustomer(CustomerUpdateDto customerDto, Long customerId) {
        customerRepo.findById(customerId).ifPresentOrElse((customer) -> {
            customer.setCustomerName(customerDto.getCustomerName());
            customer.setAddress(customerDto.getAddress());
            customer.setEmailId(customerDto.getEmailId());
            customer.setMobileNumber(customerDto.getMobileNumber());
            customer.setPassword(customerDto.getPassword());
            if (customerDto.getMobileNumber().length() == 10) {
                customerRepo.save(customer);
            } else {
                throw new CustomerException(ApplicationConstant.CHECK_MOBILE_NUMBER, HttpStatus.BAD_REQUEST);
            }
        }, () -> {
            throw new CustomerException(ApplicationConstant.CUSTOMER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        });
        return ApplicationConstant.CUSTOMER_UPDATED;
    }

    @Override
    public String deleteCustomer(Long customerId) {
        customerRepo.findById(customerId).ifPresentOrElse((customer) -> {
            List<Account> accounts = accountRepo.findByCustomer(customer);
            if (!CollectionUtils.isEmpty(accounts)) {
                throw new CustomerException(ApplicationConstant.CANT_DELETE_CUSTOMER_BECAUSE_ACCOUNT_LINKED_WITH_CUSTOMER, HttpStatus.CONFLICT);
            } else {
                customerRepo.deleteById(customerId);
            }
        }, () -> {
            throw new CustomerException(ApplicationConstant.CUSTOMER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        });
        return ApplicationConstant.CUSTOMER_DELETED;
    }
}

