package bankingapplication.service.impl;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.exception.CustomerException;
import bankingapplication.model.dto.CustomerDto;
import bankingapplication.model.dto.CustomerUpdateDto;
import bankingapplication.model.dto.MoneyTransferDto;
import bankingapplication.model.entity.Account;
import bankingapplication.model.entity.Bank;
import bankingapplication.model.entity.Customer;
import bankingapplication.model.enumType.SavingOrCurrentBalance;
import bankingapplication.repo.AccountRepo;
import bankingapplication.repo.BankRepo;
import bankingapplication.repo.CustomerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {
    @InjectMocks
    CustomerServiceImpl customerService;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private BankRepo bankRepo;

    @Test
    void save() {
        CustomerDto customerDto =new CustomerDto();
        customerDto.setCustomerId(1L); customerDto.setCustomerName("Test");
        customerDto.setPanCardNumber ("FMIPB5288J");
        customerDto.setAddress("Address");
        customerDto.setBankId(1l);
        customerDto.setAadhaarNumber ("490829761474");
        customerDto.setEmailId("test@email.com"); customerDto.setMobileNumber("1234567890");
        customerDto.setPassword("Pankaj@123");
        Customer customer = new Customer();
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);
        Bank bank = new Bank();

        Mockito.when( customerRepo.findByPanCardNumberOrAadhaarNumber(
                customerDto.getAadhaarNumber(),
                customerDto.getPanCardNumber())).thenReturn(customerList);
        Mockito.when(customerRepo.findByMobileNumberOrEmailId (customerDto.getMobileNumber(),
                customerDto.getEmailId())).thenReturn( null);
        Mockito.when(bankRepo.findById(customerDto.getBankId())).thenReturn(Optional.of (bank));
        Mockito.when(customerRepo.findByAadhaarNumberAndPanCardNumberAndBank(
                        customerDto.getAadhaarNumber(), customerDto.getPanCardNumber(), bank))
                .thenReturn(Optional.of(new Customer()));
        assertEquals(ApplicationConstant.CUSTOMER_CREATED, customerService.save(customerDto));

        Mockito.when(bankRepo.findById(customerDto.getBankId())).thenReturn(Optional.empty());
        CustomerException bankException = assertThrows(CustomerException.class,
                () -> customerService.save(customerDto));
        assertEquals(HttpStatus.BAD_REQUEST,bankException.getHttpStatus());
    }

    @Test
    void getAllCustomer() {
        Bank bank=new Bank();
        bank.setBankId(1l);
        Customer customer=new Customer();
        customer.setBank (bank);
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);
        CustomerDto customerDto=new CustomerDto();
        List<CustomerDto> customerListDto=new ArrayList<>();
        customerListDto.add(customerDto);
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setCustomerName (customer.getCustomerName());
        customerDto.setAddress(customer.getAddress());
        customerDto.setAadhaarNumber (customer.getAadhaarNumber());
        customerDto.setPanCardNumber (customer.getPanCardNumber());
        customerDto.setMobileNumber (customer.getMobileNumber());
        customerDto.setEmailId(customer.getEmailId());
        customerDto.setBankId (1l);
        Mockito.when(customerRepo.findAll()).thenReturn(customerList);
        assertEquals(customerListDto.size(), customerService.getAllCustomer().size());

        List<Customer> customerList1 = new ArrayList<>();
        Mockito.when(customerRepo.findAll()).thenReturn(customerList1);
        CustomerException bankException = assertThrows(CustomerException.class, () -> customerService.getAllCustomer());
        assertEquals(HttpStatus.BAD_REQUEST,bankException.getHttpStatus());
    }

    @Test
    void getAllByBankId() {
        Bank bank=new Bank();
        bank.setBankId(1L);
        Customer customer=new Customer();
        customer.setBank (bank);
        List<Customer> customerList=new ArrayList<>();
        customerList.add(customer);
        CustomerDto customerDto=new CustomerDto();
        List<CustomerDto> customerDtoList=new ArrayList<>();
        customerDtoList.add(customerDto);
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setCustomerName (customer.getCustomerName());
        customerDto.setAddress(customer.getAddress());
        customerDto.setAadhaarNumber (customer.getAadhaarNumber());
        customerDto.setPanCardNumber (customer.getPanCardNumber());
        customerDto.setMobileNumber (customer.getMobileNumber());
        customerDto.setEmailId(customer.getEmailId());
        customerDto.setBankId(1L);
        Mockito.when(bankRepo.findById(1L)).thenReturn(Optional.of(bank));
        Mockito.when(customerRepo.findByBank (bank)).thenReturn(customerList);
        assertEquals(customerDtoList.size(), customerService.getAllByBankId(1L).size());

        Mockito.when(bankRepo.findById(1L)).thenReturn(Optional.empty());
        CustomerException bankException = assertThrows(CustomerException.class,
                () -> customerService.getAllByBankId(1L));
        assertEquals(HttpStatus.BAD_REQUEST,bankException.getHttpStatus());

    }

    @Test
    void getByIdAndName() {
        List<CustomerDto> customerDtoList=new ArrayList<>();
        List<Customer> customerList=new ArrayList<>();
        Customer customer=new Customer();
        Bank bank=new Bank();
        bank.setBankId(1L);
        customer.setBank(bank);
        customerList.add(customer);
        Mockito.when(customerRepo.findByTitleContent (Mockito.anyString())).thenReturn(customerList);
        assertEquals(  1, customerService.getByEmailOrName(  "pankaj").size());
        List<Customer> customerList1=new ArrayList<>();
        Mockito.when(customerRepo.findByTitleContent (Mockito.anyString())).thenReturn(customerList1);
        CustomerException bankException = assertThrows (CustomerException.class,
                () -> customerService.getByEmailOrName(  "pankaj"));
        assertEquals(HttpStatus.NOT_FOUND, bankException.getHttpStatus());
    }

    @Test
    void getAllCustomerByBankId() {
        Bank bank = new Bank();
        bank.setBankId(1L);
        Customer customer = new Customer();
        customer.setBank(bank);
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);
        Mockito.when(customerRepo.checkCustomerByBankId(Mockito.anyLong())).thenReturn(customerList);
        assertEquals(  1, customerService.getAllCustomerByBankId(1L).size());

        Mockito.when(customerRepo.checkCustomerByBankId(Mockito.anyLong())).thenReturn(ArgumentMatchers.isNull());
        CustomerException bankException = assertThrows (CustomerException.class,
                () -> customerService.getAllCustomerByBankId( 1L));
        assertEquals(HttpStatus.BAD_REQUEST, bankException.getHttpStatus());
    }

    @Test
    void updateCustomer() {
        CustomerUpdateDto customerDto=new CustomerUpdateDto();

        customerDto.setCustomerName("Test");
        customerDto.setAddress("Address");
        customerDto.setEmailId("test@email.com");
        customerDto.setMobileNumber ("1234567891");
        customerDto.setPassword("eagghads");
        Customer customer = new Customer();
        Mockito.when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        assertEquals(ApplicationConstant.CUSTOMER_UPDATED, customerService
                .updateCustomer (customerDto,  1L));
    }

    @Test
    void deleteCustomer() {
        Customer customer=new Customer();
        Mockito.when(customerRepo.findById(1l)).thenReturn(Optional. of (customer));
        assertEquals(ApplicationConstant.CUSTOMER_DELETED, customerService.deleteCustomer(1l));

        Account account=new Account();
        Customer customer1 = new Customer();
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        Mockito.when(customerRepo.findById(0L)).thenReturn(Optional.of(customer1));
        Mockito.when(accountRepo.findByCustomer(customer1)).thenReturn(accounts);
        CustomerException bankException = assertThrows (CustomerException.class, () ->
                customerService.deleteCustomer( 0L));
        assertEquals(ApplicationConstant.CANT_DELETE_CUSTOMER_BECAUSE_ACCOUNT_LINKED_WITH_CUSTOMER,
                bankException.getMessage());
        Mockito.when(customerRepo.findById(0L)).thenReturn(Optional.ofNullable(  null));
        CustomerException bankException1 = assertThrows (CustomerException.class,
                () -> customerService.deleteCustomer( 0L));
        assertEquals(ApplicationConstant.CUSTOMER_NOT_FOUND, bankException1.getMessage());
    }
}