package bankingapplication.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateDto {

    @NotBlank(message = "Please enter customer name")
    private String customerName;

    @NotBlank(message = "Please enter address ")
    private String address;

    @NotBlank(message = "Please enter mobileNumber ")
    private String mobileNumber;

    @NotBlank(message = "Please provide valid emailid")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String emailId;

    @NotBlank(message = "Password.required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",message = "Password should be in 8 words including Numeric,alphabets and special character")
    private String password;
}
