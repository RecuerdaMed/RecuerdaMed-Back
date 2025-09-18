package com.hackaton.recuerdamed.drug.service;

import com.hackaton.recuerdamed.drug.dto.DrugRequest;
import com.hackaton.recuerdamed.drug.dto.DrugResponse;

import java.util.List;

public interface DrugService {
    List<DrugResponse> getAllDrugs();

    DrugResponse getDrugById(Long id);

    DrugResponse createDrug(DrugRequest request);

    DrugResponse updateDrug(Long id, DrugRequest request);

    void deleteDrug(Long id);

    void markAsTaken(Long id);
}
