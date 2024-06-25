package com.emp.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "DEPARTMENT")
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_id_generator")
    @SequenceGenerator(name = "department_id_generator", sequenceName = "SEQUENCE_DEPARTMENT", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "MANDATORY")
    private Boolean mandatory;

    @NotNull
    @Column(name = "READ_ONLY")
    private Boolean readOnly;

    @JsonIgnore
    @ManyToMany(mappedBy = "departments")
    private List<Employee> employees;
}
