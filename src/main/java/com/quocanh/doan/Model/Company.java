package com.quocanh.doan.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "companies")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull (message = "Phone number must be provided.")
    @Pattern(regexp = "^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$", message="Mobile phone number is not valid.")
    private String phone;

    @NotNull(message = "Company name must be provided.")
    @Pattern(regexp = "^[_A-Za-z0-9-]{2,}$", message="Company name is not valid.")
    private String nameCompany;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "address")
    @NotNull(message = "Address must be provided.")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    public Company() {}
    public Company(User user, Address address, String nameCompany, String phone) {
        this.user = user;
        this.address = address;
        this.nameCompany = nameCompany;
        this.phone = phone;
    }

}
