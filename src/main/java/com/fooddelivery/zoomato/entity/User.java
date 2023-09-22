package com.fooddelivery.zoomato.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @NotBlank(message = "User name cannot be blank")
    @Pattern(regexp = "^[a-z0-9]+$", message = "User name must contain only lowercase letters and numbers, with no spaces")
    private String userName;
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String userFirstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 0, max = 50, message = "Last name must be between 2 and 50 characters")
    private String userLastName;
    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits.")
    private String phoneNumber;
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-z0-9](?:[a-z0-9+.-]*[a-z0-9])?@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Invalid email address format.")
    private String emailId;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^\\S+$", message = "Password must not contain spaces")
    @Size(min = 8, message = "Password must be at least 6 characters long")
    private String userPassword;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }


}
