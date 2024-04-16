package bankingapplication.model.dto;

import bankingapplication.model.enumType.SavingOrCurrentBalance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Serializable {

    private Long accountId;

    private String accNo;

    @NotNull(message = "AccountType required")
    private SavingOrCurrentBalance accountType;

    @NotNull(message = "The bankId is required")
    private Long bankId;

    @NotNull(message = "The customer Id is required")
    private Long customerId;

    @NotBlank(message = "The name is required")
    private String name;

    @Pattern(regexp = "^[A-Za-z]{4}\\d{7}$", message = "Invalid IFSC format")
    private String ifscCode;

    @NotNull(message = "Thea amount is required")
    private Double amount;

    private boolean isBlocked;

}
