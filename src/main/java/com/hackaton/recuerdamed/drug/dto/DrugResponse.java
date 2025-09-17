package com.hackaton.recuerdamed.drug.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record DrugResponse(
        String drugName,
        String dosage,
        String description,
        Integer frequencyHours,
        LocalTime nextIntakeTime,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean active,
        boolean activeReminder,
        LocalDateTime creationDate,
        LocalDateTime updateDate
) {
}
