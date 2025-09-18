package com.hackaton.recuerdamed.drug.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record DrugRequest(

        @Schema(description = "Drug name", example = "Paracetamol", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Drug name is needed")
        String drugName,

        @Schema(description = "Drug dosage", example = "500 mg", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Dosage is needed")
        String dosage,

        @Schema(description = "Drug description", example = "Antipyretic", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 500)
        @Size(max = 500, message = "Description must be shorter than 500 characters")
        String description,

        @Schema(description = "Frequency between intakes in hours", example = "8", minimum = "1", maximum = "24", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "The frequency in hours is required")
        @Min(value = 1, message = "The frequency must be at least 1 hour")
        @Max(value = 24, message = "The frequency must be at most 24 hours")
        Integer frequencyHours,

        @Schema(description = "Next intake time", example = "08:00:00", requiredMode = Schema.RequiredMode.REQUIRED, type = "string", format = "time")
        @NotNull(message = "The next dose time is required")
        LocalTime nextIntakeTime,

        @Schema(description = "Starting treatment date and time ", example = "2025-01-01T08:00:00", requiredMode = Schema.RequiredMode.REQUIRED, type = "string", format = "date-time")
        @NotNull(message = "Start date is required")
        LocalDateTime startDate,

        @Schema(description = "Ending treatment date and time", example = "2025-01-15T08:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED, type = "string", format = "date-time")
        LocalDateTime endDate,

        @Schema(description = "Indicates if reminders are active", example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "true")
        Boolean activeReminder
) {
}
