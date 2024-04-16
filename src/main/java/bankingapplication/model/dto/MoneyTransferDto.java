package bankingapplication.model.dto;

import bankingapplication.model.enumType.SavingOrCurrentBalance;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MoneyTransferDto {

    @NotNull(message = "Please enter accountNumberFrom")
    private String accountNumberFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please enter date")
    private LocalDate date;

    @NotNull(message = "Please enter accountNumberTo")
    private String accountNumberTo;

    @NotNull(message = "Please enter accountType")
    private SavingOrCurrentBalance accountType;

    @NotNull(message = "Please enter amount")
    private Double amount;

    @NotNull(message = "Please enter ifscCode")
    private String ifscCode;

    @NotNull(message = "Please enter name")
    private String name;
}
