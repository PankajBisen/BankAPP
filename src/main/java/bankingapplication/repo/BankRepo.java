package bankingapplication.repo;

import bankingapplication.model.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepo extends JpaRepository<Bank, Long> {

    Optional<Bank> findByIfscCode(String ifscCode);

    @Query(value = "select * from bank  WHERE bank_name like :key or ifsc_code like :key ", nativeQuery = true)
    Optional<List<Bank>> findByTitleContent(@Param("key") String content);

}
