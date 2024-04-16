package bankingapplication.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CustomerDto {

    private Long customerId;

    @NotBlank(message = "Please enter the customer name")
    private String customerName;

    @NotBlank(message = "Please enter address ")
    private String address;

    @Pattern(regexp = "^[A-Z]{5}\\d{4}+[A-Z]{1}$", message = "Please enter valid  panCardNumber ")
    private String panCardNumber;

    @NotBlank(message = "Please enter aadhaarNumber ")
    private String aadhaarNumber;

    @NotBlank(message = "Please enter mobileNumber ")
    @Pattern(regexp = "\\d{10}", message = "Mobile number should be in digit and size - 10")
    private String mobileNumber;

    @NotBlank(message = "Please provide valid emailId")
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",message = "Please enter email in valid format")
    private String emailId;

    @NotNull(message = "Please enter bankId ")
    private Long bankId;

    @NotBlank(message = "Password.required")
    @Size(min = 8, message = "{password.length.required}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",message = "Password should be in 8 words including Numeric,alphabets and special character")
    private String password;
}
