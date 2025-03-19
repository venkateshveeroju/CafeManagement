package com.restaurant.tablebookingapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@SecondaryTables(@SecondaryTable(name = "roles"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(table = "roles", name = "id", insertable = false, updatable = false)
    private Long roleid;

    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;


}