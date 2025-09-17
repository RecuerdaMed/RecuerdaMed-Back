package com.hackaton.recuerdamed.drug.service;

import com.hackaton.recuerdamed.drug.dto.DrugResponse;

import java.util.List;

public interface DrugService {
    List<DrugResponse> getAllDrugs();

    DrugResponse getDrugById(Long id);

}
