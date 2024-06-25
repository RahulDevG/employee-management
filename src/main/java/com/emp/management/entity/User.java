package com.emp.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "USER_LOGIN")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "SEQUENCE_USER", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "PASSWORD", nullable = false)
    private String password;
}
