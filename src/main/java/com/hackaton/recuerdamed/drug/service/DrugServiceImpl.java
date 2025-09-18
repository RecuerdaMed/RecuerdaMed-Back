package com.hackaton.recuerdamed.drug.service;

import com.hackaton.recuerdamed.drug.dto.DrugMapper;
import com.hackaton.recuerdamed.drug.dto.DrugRequest;
import com.hackaton.recuerdamed.drug.dto.DrugResponse;
import com.hackaton.recuerdamed.drug.entity.Drug;
import com.hackaton.recuerdamed.drug.repository.DrugRepository;
import com.hackaton.recuerdamed.shared.custom_exception.DrugNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugServiceImpl implements DrugService {
    private final DrugRepository drugRepository;
    private final DrugMapper drugMapper;

    @Override
    public List<DrugResponse> getAllDrugs() {
        return drugRepository.findByActiveTrueOrderByNextIntakeTimeAsc()
                .stream()
                .map(drugMapper::toDto)
                .toList();
    }

    @Override
    public DrugResponse getDrugById(Long id) {
        Drug drug = drugRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new DrugNotFoundException("Drug with ID: " + id + " not found"));
        return drugMapper.toDto(drug);
    }

    @Override
    @Transactional
    public DrugResponse createDrug(DrugRequest request) {
        Drug drug = drugMapper.toEntity(request);
        Drug savedDrug = drugRepository.save(drug);
        return drugMapper.toDto(savedDrug);
    }

    @Override
    @Transactional
    public DrugResponse updateDrug(Long id, DrugRequest request) {
        Drug drug = drugRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new DrugNotFoundException("Drug with ID: " + id + " not found"));
        drugMapper.updateEntityFromRequest(drug, request);
        Drug updatedDrug = drugRepository.save(drug);
        return drugMapper.toDto(updatedDrug);
    }

    @Override
    @Transactional
    public void deleteDrug(Long id) {
        Drug drug = drugRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new DrugNotFoundException("Drug with ID: " + id + " not found"));

        drug.setActive(false);
        drugRepository.save(drug);
    }

    @Override
    @Transactional
    public void markAsTaken(Long id) {
        Drug drug = drugRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new DrugNotFoundException("Drug with ID: " + id + " not found"));
        LocalTime nextIntake = drug.getNextIntakeTime().plusHours(drug.getFrequencyHours());
        drug.setNextIntakeTime(nextIntake);
        drugRepository.save(drug);
    }

    @Override
    @Transactional
    public List<DrugResponse> searchByName (String drugName) {
        return drugRepository.findByDrugNameContainingIgnoreCaseAndActiveTrue(drugName).stream().map(drug -> drugMapper.toDto(drug)).toList();
    }
}
