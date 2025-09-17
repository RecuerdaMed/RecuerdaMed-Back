package com.hackaton.recuerdamed.drug.dto;

import com.hackaton.recuerdamed.drug.entity.Drug;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DrugMapper {

    public Drug toEntity(DrugRequest request) {
        return Drug.builder()
                .drugName(request.drugName().trim())
                .description(request.description())
                .dosage(request.dosage().trim())
                .frequencyHours(request.frequencyHours())
                .nextIntakeTime(request.nextIntakeTime())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .active(true)
                .activeReminder(request.activeReminder())
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    public DrugResponse toDto(Drug drug) {
        return new DrugResponse(
                drug.getId(),
                drug.getDrugName(),
                drug.getDescription(),
                drug.getDosage(),
                drug.getFrequencyHours(),
                drug.getNextIntakeTime(),
                drug.getStartDate(),
                drug.getEndDate(),
                drug.getActive(),
                drug.getActiveReminder(),
                drug.getCreationDate(),
                drug.getUpdateDate()
        );
    }

    public void updateEntityFromRequest(Drug drug, DrugRequest request) {
        drug.setDrugName(request.drugName().trim());
        drug.setDosage(request.dosage().trim());
        drug.setDescription(request.description() != null ? request.description().trim() : null);
        drug.setFrequencyHours(request.frequencyHours());
        drug.setNextIntakeTime(request.nextIntakeTime());
        drug.setStartDate(request.startDate());
        drug.setEndDate(request.endDate());
        drug.setActiveReminder(request.activeReminder() != null ? request.activeReminder() : true);
    }
}
