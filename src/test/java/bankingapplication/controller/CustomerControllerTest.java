package bankingapplication.controller;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.model.dto.CustomerDto;
import bankingapplication.model.dto.CustomerUpdateDto;
import bankingapplication.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class CustomerControllerTest {
    @InjectMocks
    CustomerController customerController;

    @Mock
    CustomerService customerService;



    @Test
    void save() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(1L);
        customerDto.setCustomerName("Test");
        customerDto.setPanCardNumber("LUDPS1234K");
        customerDto.setAddress("Address");
        customerDto.setBankId(1L);
        customerDto.setAadhaarNumber("987654321234");
        customerDto.setEmailId("test@email.com");
        customerDto.setMobileNumber("1234567890");
        customerDto.setPassword("eagghads");
        Mockito.when(customerService.save(customerDto)).thenReturn(ApplicationConstant.CUSTOMER_CREATED);
        ResponseEntity<String> save = customerController.save(customerDto);
        assertEquals(ApplicationConstant.CUSTOMER_CREATED, save.getBody());
    }

    @Test
    void getByIdAndName() {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        Mockito.when(customerService.getByEmailOrName("pankaj")).thenReturn(customerDtoList);
        ResponseEntity<List<CustomerDto>> listResponseEntity = customerController.getByEmailOrName("pankaj");

        assertEquals(customerDtoList.size(), listResponseEntity.getBody().size());
    }

    @Test
    void getAllCustomer() {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        Mockito.when(customerService.getAllCustomer()).thenReturn(customerDtoList);
        ResponseEntity<List<CustomerDto>> allCustomer = customerController.getAllCustomer();
        assertEquals(customerDtoList.size(), allCustomer.getBody().size());
    }

    @Test
    void updateCustomer() {
        CustomerUpdateDto customerDto = new CustomerUpdateDto();
        customerDto.setCustomerName("Test ");
        customerDto.setEmailId("test@email.com");
        customerDto.setMobileNumber("1234567890");
        customerDto.setPassword("eagghads");
        Mockito.when(customerService.updateCustomer(customerDto, 1L)).thenReturn(ApplicationConstant.CUSTOMER_UPDATED);
        ResponseEntity<String> stringResponseEntity = customerController.updateCustomer(customerDto, 1L);
        assertEquals(ApplicationConstant.CUSTOMER_UPDATED, stringResponseEntity.getBody());


        customerDto.setAddress("Address");
    }

    @Test
    void deleteCustomer() {
        Mockito.when(customerService.deleteCustomer(1L)).thenReturn(ApplicationConstant.CUSTOMER_DELETED);
        ResponseEntity<String> stringResponseEntity = customerController.deleteCustomer(1L);
        assertEquals(ApplicationConstant.CUSTOMER_DELETED, stringResponseEntity.getBody());

    }

    @Test
    void testGetAllCustomer() {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        Mockito.when(customerService.getAllCustomerByBankId(1L)).thenReturn(customerDtoList);
        ResponseEntity<List<CustomerDto>> allCustomer = customerController.getAllCustomerByBankId(1L);
        assertEquals(customerDtoList.size(), allCustomer.getBody().size());
    }

    @Test
    void getAllByBankId() {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        Mockito.when(customerService.getAllByBankId(1L)).thenReturn(customerDtoList);
        ResponseEntity<List<CustomerDto>> allByBankId = customerController.getAllByBankId(1L);
        assertEquals(customerDtoList.size(), allByBankId.getBody().size());
    }

    @Test
    void getCustomerService() {

    }
}