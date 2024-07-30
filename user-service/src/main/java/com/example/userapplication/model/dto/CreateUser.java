package com.example.userapplication.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateUser {

    private String firstName;

    private String lastName;

    private String contactNumber;

    private String emailId;

    private String password;
}
