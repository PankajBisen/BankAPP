package bankingapplication.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "customerId")
    private Long customerId;
    private String customerName;
    private String aadhaarNumber;
    private String address;
    private String panCardNumber;
    private String mobileNumber;
    private String emailId;
    private String password;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bankId", referencedColumnName = "bankId")
    private Bank bank;

}