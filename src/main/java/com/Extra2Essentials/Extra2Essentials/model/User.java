package com.Extra2Essentials.Extra2Essentials.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private String resetToken;

    private String name;

    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String city;
    private String phone;
    private String address;
    private Double latitude;
    private Double longitude;

    private boolean verified = false;
}