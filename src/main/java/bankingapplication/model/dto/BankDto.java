package bankingapplication.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BankDto {

    @NotBlank(message = "The bankName  is required")
    private String bankName;

    @Pattern(regexp = "^[A-Z]{4}\\d{7}$", message = "Invalid IFSC format")
    private String ifscCode;

    @NotBlank(message = "The branchName is required")
    private String  branchName;

    @NotBlank(message = "The address is required")
    private String address;

    @NotBlank(message = "The city is required")
    private String city;
}
