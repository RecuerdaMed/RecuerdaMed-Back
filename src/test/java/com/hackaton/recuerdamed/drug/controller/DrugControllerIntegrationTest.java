package com.hackaton.recuerdamed.drug.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackaton.recuerdamed.drug.dto.DrugRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DrugControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private DrugRequest validRequest;

    @BeforeEach
    void setUp(){
        validRequest = new DrugRequest(
                "Amoxicilina",
                "500mg",
                "Antibiótico para infecciones bacterianas",
                12,
                LocalTime.of(9, 0,0),
                LocalDateTime.of(2025, 9, 17, 9, 0),
                LocalDateTime.of(2025, 9, 24, 9, 0),
                true
        );
    }

    private String asJsonString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to convert object to JSON string for testing", exception);
        }
    }

    private ResultActions performPostRequest(Object body) throws Exception{
        return mockMvc.perform(post("/medicamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(body)));
    }

    private ResultActions performPutRequest(Long id, Object body) throws Exception{
        return mockMvc.perform(put("/medicamentos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(body)));
    }

    @Nested
    @DisplayName("GET /medicamentos")
    class GetAllDrugsTests {
        @Test
        @DisplayName("should return all active drugs ordered by nextIntakeTime")
        void getAllDrugs_returnsListOfDrugs() throws Exception{
            mockMvc.perform(get("/medicamentos"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].drugName", is("Paracetamol")))
                    .andExpect(jsonPath("$[1].drugName", is("Ibuprofeno")));
        }
    }

    @Nested
    @DisplayName("GET /medicamentos/{id}")
    class GetDrugByIdTests {
        @Test
        @DisplayName("should return drug details when the id exists")
        void getDrugById_returnsDrugDetails() throws Exception {
            mockMvc.perform(get("/medicamentos/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.drugName", is("Paracetamol")))
                    .andExpect(jsonPath("$.description", is("Analgesico y antipiretico")))
                    .andExpect(jsonPath("$.dosage", is("500mg")))
                    .andExpect(jsonPath("$.active", is(true)))
                    .andExpect(jsonPath("$.activeReminder", is(true)));
        }

        @Test
        @DisplayName("should return 404 when the id does not exist")
        void getDrugById_returnsNotFound_WhenIdDoesNotExist() throws Exception {
            mockMvc.perform(get("/medicamentos/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /medicamentos")
    class CreateDrugTests {
        @Test
        @DisplayName("should create a drug correctly and return 201")
        void createDrug_returnsCreatedDrug() throws Exception{
            performPostRequest(validRequest)
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.drugName", is(validRequest.drugName())))
                    .andExpect(jsonPath("$.dosage", is(validRequest.dosage())))
                    .andExpect(jsonPath("$.description", is(validRequest.description())))
                    .andExpect(jsonPath("$.frequencyHours", is(validRequest.frequencyHours())))
                    .andExpect(jsonPath("$.nextIntakeTime", is("09:00:00")))
                    .andExpect(jsonPath("$.activeReminder", is(validRequest.activeReminder())));
        }

        @Test
        @DisplayName("Should return 400 when drugName is missing")
        void createDrug_missingDrugName_returnsBadRequest() throws Exception{
            DrugRequest request = new DrugRequest(
                    "",
                    "250mg",
                    "Analgésico",
                    8,
                    LocalTime.of(8, 0),
                    LocalDateTime.of(2025, 9, 17, 8, 0),
                    null,
                    true
            );
            performPostRequest(request)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when dosage is missing")
        void createDrug_missingDosage_returnsBadRequest() throws Exception{
            DrugRequest request = new DrugRequest(
                    "Ibuprofeno",
                    "",
                    "Analgésico",
                    8,
                    LocalTime.of(8, 0),
                    LocalDateTime.of(2025, 9, 17, 8, 0),
                    null,
                    true
            );
            performPostRequest(request)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when frequencyHours is invalid")
        void createDrug_invalidFrequency_returnsBadRequest() throws Exception{
            DrugRequest request = new DrugRequest(
                    "Ibuprofeno",
                    "250mg",
                    "Analgésico",
                    0,
                    LocalTime.of(8, 0),
                    LocalDateTime.of(2025, 9, 17, 8, 0),
                    null,
                    true
            );
            performPostRequest(request)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when nextIntakeTime is missing")
        void createDrug_missingNextIntake_returnsBadRequest() throws Exception{
            DrugRequest request = new DrugRequest(
                    "Ibuprofeno",
                    "250mg",
                    "Analgésico",
                    12,
                    null,
                    LocalDateTime.of(2025, 9, 17, 8, 0),
                    null,
                    true
            );
            performPostRequest(request)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when startDate is missing")
        void createDrug_missingStartDate_returnsBadRequest() throws Exception{
            DrugRequest request = new DrugRequest(
                    "Ibuprofeno",
                    "250mg",
                    "Analgésico",
                    12,
                    LocalTime.of(6, 0),
                    null,
                    null,
                    true
            );
            performPostRequest(request)
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /medicamentos/{id}")
    class UpdateDurgTests {
        @Test
        @DisplayName("should update drug successfully and return 200")
        void updateDrug_success() throws Exception{
            DrugRequest request = new DrugRequest(
                    "Paracetamol Actualizado",
                    "650mg",
                    "Actualizado: analgésico más potente",
                    6,
                    LocalTime.of(10, 0),
                    LocalDateTime.of(2025, 9, 20, 10, 0),
                    LocalDateTime.of(2025, 9, 25, 10, 0),
                    false
            );

            performPutRequest(1L, request)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.drugName", is("Paracetamol Actualizado")))
                    .andExpect(jsonPath("$.dosage", is("650mg")))
                    .andExpect(jsonPath("$.description", is("Actualizado: analgésico más potente")))
                    .andExpect(jsonPath("$.frequencyHours", is(6)))
                    .andExpect(jsonPath("$.nextIntakeTime", is("10:00:00")))
                    .andExpect(jsonPath("$.activeReminder", is(false)));
        }

        @Test
        @DisplayName("should return 404 when trying to update non-existing drug")
        void updateDrug_notFound() throws Exception{
            DrugRequest request = new DrugRequest(
                    "Nuevo Medicamento",
                    "200mg",
                    "Descripción",
                    8,
                    LocalTime.of(8, 0),
                    LocalDateTime.of(2025, 9, 20, 8, 0),
                    LocalDateTime.of(2025, 9, 22, 8, 0),
                    true
            );

            performPutRequest(99L, request)
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return 400 when updating with invalid data (missing drugName)")
        void updateDrug_invalidData_returnsBadRequest() throws Exception{
            DrugRequest request = new DrugRequest(
                    "",
                    "500mg",
                    "Descripción inválida",
                    8,
                    LocalTime.of(8, 0),
                    LocalDateTime.of(2025, 9, 20, 8, 0),
                    null,
                    true
            );

            performPutRequest(1L, request)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when updating with invalid frequencyHours")
        void updateDrug_invalidFrequency_returnsBadRequest() throws Exception{
            DrugRequest request = new DrugRequest(
                    "Ibuprofeno",
                    "400mg",
                    "Frecuencia inválida",
                    0,
                    LocalTime.of(12, 0),
                    LocalDateTime.of(2025, 9, 20, 12, 0),
                    null,
                    true
            );

            performPutRequest(1L, request)
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /medicamentos/{id}")
    class DeleteDrugTests {
        @Test
        @DisplayName("should delete drug (set active to false) and return 204")
        void deleteDrug_success() throws Exception{
            mockMvc.perform(delete("/medicamentos/1"))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/medicamentos/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return 404 when trying to delete non-existing drug")
        void deleteDrug_notFound() throws Exception{
            mockMvc.perform(delete("/medicamentos/99"))
                    .andExpect(status().isNotFound());
        }
    }
}