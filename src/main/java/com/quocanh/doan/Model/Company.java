package com.quocanh.doan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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


    @NotNull(message = "Phone number must be provided.")
    @Pattern(regexp = "^(((\\+|)84)|0)(3|5|7|8|9)+([0-9]{8})$", message="Mobile phone number is not valid.")
    private String phone;

    @NotNull(message = "Company name must be provided.")
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
