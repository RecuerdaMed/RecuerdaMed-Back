package com.hackaton.recuerdamed.drug.controller;

import com.hackaton.recuerdamed.drug.dto.DrugResponse;
import com.hackaton.recuerdamed.drug.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medicamentos")
public class DrugController {
    private final DrugService drugService;

    @GetMapping
    public ResponseEntity<List<DrugResponse>> getAllDrugs(){
        List<DrugResponse> drugs = drugService.getAllDrugs();
        return ResponseEntity.ok(drugs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrugResponse> getDrugById(@PathVariable Long id){
        DrugResponse drug = drugService.getDrugById(id);
        return ResponseEntity.ok(drug);
    }
}