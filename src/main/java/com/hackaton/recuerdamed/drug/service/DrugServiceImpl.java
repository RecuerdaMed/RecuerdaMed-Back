package com.hackaton.recuerdamed.drug.service;

import com.hackaton.recuerdamed.drug.dto.DrugMapper;
import com.hackaton.recuerdamed.drug.dto.DrugResponse;
import com.hackaton.recuerdamed.drug.entity.Drug;
import com.hackaton.recuerdamed.drug.repository.DrugRepository;
import com.hackaton.recuerdamed.shared.exception.custom_exception.DrugNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

    @Override
    public DrugResponse getDrugById(Long id) {
        Drug drug = drugRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new DrugNotFoundException("Drug not found with ID:" + id));
        return drugMapper.toDto(drug);
    }
}
