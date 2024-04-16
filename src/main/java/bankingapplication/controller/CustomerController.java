package bankingapplication.controller;

import bankingapplication.constant.UrlConstant;
import bankingapplication.model.dto.CustomerDto;
import bankingapplication.model.dto.CustomerUpdateDto;
import bankingapplication.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(UrlConstant.CUSTOMER_URL)
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(UrlConstant.CUSTOMER_CREATE)
    public ResponseEntity<String> save(@Valid @RequestBody CustomerDto customerDto) {
        return new ResponseEntity<>(customerService.save(customerDto), HttpStatus.CREATED);
    }

    @GetMapping(UrlConstant.GET_BY_NAME_OR_MOBILENO_OR_EMAILID)
    public ResponseEntity<List<CustomerDto>> getByEmailOrName(@PathVariable String content) {
        return new ResponseEntity<>(customerService.getByEmailOrName(content), HttpStatus.OK);
    }

    @GetMapping(UrlConstant.GET_ALL)
    public ResponseEntity<List<CustomerDto>> getAllCustomer() {
        return new ResponseEntity<>(customerService.getAllCustomer(), HttpStatus.OK);
    }

    @PutMapping(UrlConstant.UPDATE_CUSTOMER)
    public ResponseEntity<String> updateCustomer(@Valid @RequestBody CustomerUpdateDto customerDto, @PathVariable Long customerId) {
        return new ResponseEntity<>(customerService.updateCustomer(customerDto, customerId), HttpStatus.OK);
    }

    @DeleteMapping(UrlConstant.DELETE_CUSTOMER)
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        return new ResponseEntity<>(customerService.deleteCustomer(customerId), HttpStatus.OK);
    }

    @GetMapping(UrlConstant.GET_ALL_CUSTOMERS_WITHOUT_ACCOUNTS)
    public ResponseEntity<List<CustomerDto>> getAllCustomerByBankId(@PathVariable Long bankId) {
        return new ResponseEntity<>(customerService.getAllCustomerByBankId(bankId), HttpStatus.OK);
    }

    @GetMapping(UrlConstant.GET_ALL_CUSTOMER_BY_BANK_ID)
    public ResponseEntity<List<CustomerDto>> getAllByBankId(@PathVariable Long bankId) {
        return new ResponseEntity<>(customerService.getAllByBankId(bankId), HttpStatus.OK);
    }
}
