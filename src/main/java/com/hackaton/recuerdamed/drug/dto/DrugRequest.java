package com.hackaton.recuerdamed.drug.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record DrugRequest(
        @NotBlank(message = "Drug name is needed")
        String drugName,

        @NotBlank(message = "Dosage is needed")
        String dosage,

        @Size(max = 500, message = "Description must be shorter than 500 characters")
        String description,

        @NotNull(message = "The frequency in hours is required")
        @Min(value = 1, message = "The frequency must be at least 1 hour")
        @Max(value = 24, message = "The frequency must be at most 24 hours")
        Integer frequencyHours,

        @NotNull(message = "The next dose time is required")
        LocalTime nextIntakeTime,

        @NotNull(message = "Start date is required")
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean activeReminder
) {
}
