package com.megacityCab.megaCityCabBackEnd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "user_role_details")
public class userHasRoles {

    @Id
    @Column(name = "userHasRoleId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleDetailsId;

    @Column(name = "userRole")
    @NonNull
    private Long userRole;

    @Column(name = "userId")
    @NonNull
    private Long userId;

    @Column(name = "status")
    @NonNull
    private String status;
}
