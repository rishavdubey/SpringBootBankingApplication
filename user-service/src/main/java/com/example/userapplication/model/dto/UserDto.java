package com.example.userapplication.model.dto;

import com.example.userapplication.model.Status;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {

    private Long userId;

    private String emailId;

    private String password;

    private String identificationNumber;

    private String authId;

    private Status status;

    private UserProfileDto userProfileDto;

}
