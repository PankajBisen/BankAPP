package bankingapplication.repo;

import bankingapplication.model.entity.Bank;
import bankingapplication.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    List<Customer> findByPanCardNumberOrAadhaarNumber(String panNumber, String aadharNumber);

    List<Customer> findByMobileNumberOrEmailId(String mobileNumber, String emailId);

    List<Customer> findByBank(Bank bank);

    Optional<Customer> findByAadhaarNumberAndPanCardNumberAndBank(String aadhar, String pan, Bank bank);

    @Query(value = "select * from customer  WHERE customer_name like :key or mobile_number like :key or email_id like :key ", nativeQuery = true)
    List<Customer> findByTitleContent(@Param("key") String content);

    @Query(value = "SELECT C.* FROM public.customer C \n" + "LEFT JOIN public.account a on a.customer_id = c.customer_id\n" + "WHERE C.bank_id = :bankId and a.customer_id is null", nativeQuery = true)
    List<Customer> checkCustomerByBankId(@Param("bankId") long bankId);
}
