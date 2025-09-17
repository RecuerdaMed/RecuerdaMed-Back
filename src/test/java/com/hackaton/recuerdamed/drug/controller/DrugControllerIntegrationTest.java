package com.hackaton.recuerdamed.drug.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DrugControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
}