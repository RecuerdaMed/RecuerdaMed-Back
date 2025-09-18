package com.hackaton.recuerdamed.drug.controller;

import com.hackaton.recuerdamed.drug.dto.DrugRequest;
import com.hackaton.recuerdamed.drug.dto.DrugResponse;
import com.hackaton.recuerdamed.drug.service.DrugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<DrugResponse> createDrug(@Valid @RequestBody DrugRequest request){
        DrugResponse drug = drugService.createDrug(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(drug);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DrugResponse> updateDrug(@PathVariable Long id, @Valid @RequestBody DrugRequest request) {
        DrugResponse drug = drugService.updateDrug(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(drug);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrug(@PathVariable Long id){
        drugService.deleteDrug(id);
        return ResponseEntity.noContent().build();
    }
}