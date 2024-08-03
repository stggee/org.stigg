package com.stigg.lernwortspiel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String countryCode;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;

}
