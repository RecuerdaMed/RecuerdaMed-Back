package com.hackaton.recuerdamed.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "drugs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "drug_name", length = 100)
    private String drugName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double dosage;

    /*@Column(nullable = false, name = "intake_time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime intakeTime;

    @Column(nullable = false, name = "last_intake")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastIntakeDate;*/

    @Column(nullable = false, name = "taken_today")
    private Boolean takenToday = false;







}
