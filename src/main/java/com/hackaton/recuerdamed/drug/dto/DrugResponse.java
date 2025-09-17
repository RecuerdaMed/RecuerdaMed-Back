package com.hackaton.recuerdamed.drug.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record DrugResponse(
        Long id,
        String drugName,
        String description,
        String dosage,
        Integer frequencyHours,
        LocalTime nextIntakeTime,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean active,
        Boolean activeReminder,
        LocalDateTime creationDate,
        LocalDateTime updateDate
) {
}
