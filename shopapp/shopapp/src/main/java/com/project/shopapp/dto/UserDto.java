package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotEmpty(message = "email Not empty")
    @Email
    @JsonProperty("email")
    private String email;
    @NotEmpty(message = "name not empty")
    @Size(min = 2, max = 150, message = "name must be range 2 - 150 characters")
    @JsonProperty("full_name")
    private String fullName;
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$")
    @NotBlank
    @JsonProperty(value = "phone_number")
    private String phoneNumber;
    @NotBlank(message = "password do not blank")
    @JsonProperty("password")
    private String password;
    @NotBlank(message = "Retype Password do not blank")
    @JsonProperty("retype_password")
    private String retypePassword;
   @NotBlank(message = "Address do not blank")
   @JsonProperty("address")
    private String address;
   @JsonProperty(value = "date_of_birth")
    private Date dateOfBirth;
   @JsonProperty("facebook_account_id")
    private Integer facebookAccountId;
   @JsonProperty("google_account_id")
    private Integer googleAccountId;
   @JsonProperty("role_id")
    private Integer roleId;
}
