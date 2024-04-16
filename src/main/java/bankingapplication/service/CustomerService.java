package bankingapplication.service;

import bankingapplication.model.dto.CustomerDto;
import bankingapplication.model.dto.CustomerUpdateDto;

import java.util.List;


public interface CustomerService {

  String save(CustomerDto customerDto);

  List<CustomerDto> getByEmailOrName(String content);

  List<CustomerDto> getAllCustomer();

  String updateCustomer(CustomerUpdateDto customerUpdateDto, Long customerId);

  String deleteCustomer(Long customerId);

  List<CustomerDto> getAllCustomerByBankId(Long bankId);

  List<CustomerDto> getAllByBankId(Long bankId);
}