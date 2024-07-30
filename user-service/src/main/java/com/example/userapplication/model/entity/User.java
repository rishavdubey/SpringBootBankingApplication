package com.example.userapplication.model.entity;

import com.example.userapplication.model.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
@Table(name="user_bank")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String emailId;

    private String contactNo;

    private String authId;

    private String identificationNumber;

    @CreationTimestamp
    private LocalDate creationOn;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "userProfileId")
    private UserProfile userProfile;
}