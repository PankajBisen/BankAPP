package bankingapplication.repo;

import bankingapplication.model.entity.Transaction;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

  List<Transaction> findByAccountNumberFrom(String accNo);

  List<Transaction> findByDateBetween(LocalDate localDate, LocalDate endDate);
}
