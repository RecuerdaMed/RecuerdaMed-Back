package com.hackaton.recuerdamed.drug.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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

    @Column(nullable = false, name = "drug_name")
    private String drugName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false, name = "frequency_hours")
    private Integer frequencyHours;

    @Column(nullable = false, name = "next_intake_time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime nextIntakeTime;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activeReminder = true;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updateDate = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
