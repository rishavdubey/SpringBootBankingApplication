package com.example.userapplication.model.dto;

import com.example.userapplication.model.Status;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserUpdateStatus {

    private Status status;
}
