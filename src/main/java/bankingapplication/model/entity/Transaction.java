package bankingapplication.model.entity;

import bankingapplication.model.enumType.SavingOrCurrentBalance;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String accountNumberFrom;
    private String accountNumberTo;
    private SavingOrCurrentBalance accountType;
    private double amount;
    private String ifscCode;
    private String name;
    private boolean isBlocked;

}
