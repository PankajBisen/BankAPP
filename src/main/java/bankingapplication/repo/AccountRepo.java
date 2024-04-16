package bankingapplication.repo;

import bankingapplication.model.entity.Account;
import bankingapplication.model.entity.Bank;
import bankingapplication.model.entity.Customer;
import bankingapplication.model.enumType.SavingOrCurrentBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findByAccNo(String accNo);

    List<Account> findByCustomerAndBankAndAccountType(Customer customer, Bank bank, SavingOrCurrentBalance accountType);

    List<Account> findByBank(Bank bank);

    List<Account> findByCustomer(Customer customer);

    @Query(value = "select * from account  WHERE  acc_no like :key", nativeQuery = true)
    List<Account> findByTitleContent(@Param("key") String content);
}
