package com.hackaton.recuerdamed.drug.controller;

import com.hackaton.recuerdamed.drug.dto.DrugRequest;
import com.hackaton.recuerdamed.drug.dto.DrugResponse;
import com.hackaton.recuerdamed.drug.service.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medicamentos")
@Slf4j
@Tag(name= "Drugs", description = "Operations related to drugs and drugs intake management")
public class DrugController {
    private final DrugService drugService;

    @Operation(summary = "Get all drugs", description = "Return a list of all active drugs ordered by next intake time")
    @GetMapping
    public ResponseEntity<List<DrugResponse>> getAllDrugs(){
        List<DrugResponse> drugs = drugService.getAllDrugs();
        return ResponseEntity.ok(drugs);
    }

    @Operation(summary = "Get drug by ID", description = "Return details of a specific drug selected by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DrugResponse> getDrugById(@PathVariable Long id){
        DrugResponse drug = drugService.getDrugById(id);
        return ResponseEntity.ok(drug);
    }

    @Operation(summary = "Create new drug", description = "Create a new drug with all required information in the system")
    @PostMapping
    public ResponseEntity<DrugResponse> createDrug(@Valid @RequestBody DrugRequest request){
        DrugResponse drug = drugService.createDrug(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(drug);
    }

    @Operation(summary = "Update drug by ID", description = "Actualise information of an existing drug")
    @PutMapping("/{id}")
    public ResponseEntity<DrugResponse> updateDrug(@PathVariable Long id, @Valid @RequestBody DrugRequest request) {
        DrugResponse drug = drugService.updateDrug(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(drug);
    }

    @Operation(summary = "Delete drug by ID", description = "Mark a specific drug selected by ID as inactive (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrug(@PathVariable Long id){
        drugService.deleteDrug(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mark a drug as taken", description = "Actualise the next intake of the drug according to its frequency")
    @PutMapping("/{id}/tomado")
    public ResponseEntity<Void> markAsTaken(@PathVariable Long id) {
        drugService.markAsTaken(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Search a drug by name", description = "Search drugs that contain a specific text within the name (case-insensitive)")
    @GetMapping("/buscar")
    public ResponseEntity<List<DrugResponse>> searchByName(String drugName) {
        List<DrugResponse> drugs = drugService.searchByName(drugName);
        return ResponseEntity.status(HttpStatus.OK).body(drugs);
    }

    @Operation(summary = "Process reminders", description = "Process drugs intake reminders scheduled for the current time")
    @PostMapping("/recordatorios")
    public ResponseEntity<Void> processReminders() {
        drugService.processReminders();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}