package com.hackaton.recuerdamed.drug.dto;

import com.hackaton.recuerdamed.drug.entity.Drug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for DrugMapper")
public class DrugMapperTest {
    private DrugMapper drugMapper;
    private DrugRequest sampleRequest;
    private Drug sampleDrug;

    @BeforeEach
    void setUp(){
        drugMapper = new DrugMapper();

        sampleRequest = new DrugRequest(
                " Paracetamol ",
                " 500mg ",
                " Analgésico ",
                8,
                LocalTime.of(9, 0),
                LocalDateTime.of(2025, 9, 17, 9, 0),
                LocalDateTime.of(2025, 9, 24, 9, 0),
                true
        );

        sampleDrug = Drug.builder()
                .id(1L)
                .drugName("Ibuprofeno")
                .dosage("200mg")
                .description("Para el dolor")
                .frequencyHours(12)
                .nextIntakeTime(LocalTime.of(10, 0))
                .startDate(LocalDateTime.of(2025, 9, 17, 10, 0))
                .endDate(LocalDateTime.of(2025, 9, 20, 10, 0))
                .active(true)
                .activeReminder(true)
                .creationDate(LocalDateTime.of(2025, 1, 1, 8, 0))
                .updateDate(LocalDateTime.of(2025, 1, 1, 8, 0))
                .build();
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests{
        @Test
        @DisplayName("should map DrugRequest to Drug entity correctly with trimming")
        void toEntity_success(){
            Drug result = drugMapper.toEntity(sampleRequest);

            assertNotNull(result);
            assertNotNull(result);
            assertEquals("Paracetamol", result.getDrugName());
            assertEquals("500mg", result.getDosage());
            assertEquals(" Analgésico ", result.getDescription());
            assertEquals(8, result.getFrequencyHours());
            assertEquals(LocalTime.of(9, 0), result.getNextIntakeTime());
            assertEquals(LocalDateTime.of(2025, 9, 17, 9, 0), result.getStartDate());
            assertEquals(LocalDateTime.of(2025, 9, 24, 9, 0), result.getEndDate());
            assertTrue(result.getActive());
            assertTrue(result.getActiveReminder());
            assertNotNull(result.getCreationDate());
            assertNotNull(result.getUpdateDate());
        }
    }

    @Nested
    @DisplayName("toDTO")
    class  toDtoTests {
        @Test
        @DisplayName("should map Drug entity to DrugResponse correctly")
        void toDto_success(){
            DrugResponse result = drugMapper.toDto(sampleDrug);

            assertNotNull(result);
            assertEquals(sampleDrug.getId(), result.id());
            assertEquals(sampleDrug.getDrugName(), result.drugName());
            assertEquals(sampleDrug.getDosage(), result.dosage());
            assertEquals(sampleDrug.getDescription(), result.description());
            assertEquals(sampleDrug.getFrequencyHours(), result.frequencyHours());
            assertEquals(sampleDrug.getNextIntakeTime(), result.nextIntakeTime());
            assertEquals(sampleDrug.getStartDate(), result.startDate());
            assertEquals(sampleDrug.getEndDate(), result.endDate());
            assertEquals(sampleDrug.getActive(), result.active());
            assertEquals(sampleDrug.getActiveReminder(), result.activeReminder());
            assertEquals(sampleDrug.getCreationDate(), result.creationDate());
            assertEquals(sampleDrug.getUpdateDate(), result.updateDate());
        }
    }

    @Nested
    @DisplayName("updateEntityFromRequest")
    class UpdateEntityFromRequestTests{
        @Test
        @DisplayName("should update entity with all fields from request")
        void updateEntity_success(){
            drugMapper.updateEntityFromRequest(sampleDrug, sampleRequest);

            assertEquals("Paracetamol", sampleDrug.getDrugName());
            assertEquals("500mg", sampleDrug.getDosage());
            assertEquals("Analgésico", sampleDrug.getDescription().trim());
            assertEquals(8, sampleDrug.getFrequencyHours());
            assertEquals(LocalTime.of(9, 0), sampleDrug.getNextIntakeTime());
            assertEquals(LocalDateTime.of(2025, 9, 17, 9, 0), sampleDrug.getStartDate());
            assertEquals(LocalDateTime.of(2025, 9, 24, 9, 0), sampleDrug.getEndDate());
            assertTrue(sampleDrug.getActiveReminder());
        }

        @Test
        @DisplayName("should set description to null when request is null")
        void updateEntity_nullDescription(){
            DrugRequest requestWithNullDescription = new DrugRequest(
                    "Ibuprofeno",
                    "200mg",
                    null,
                    6,
                    LocalTime.of(8, 0),
                    LocalDateTime.of(2025, 9, 18, 8, 0),
                    null,
                    true
            );

            drugMapper.updateEntityFromRequest(sampleDrug, requestWithNullDescription);

            assertNull(sampleDrug.getDescription());
        }

        @Test
        @DisplayName("should set activeReminder to true when request activeReminder is null")
        void updateEntity_nullActiveReminder(){
            DrugRequest requestWithNullReminder = new DrugRequest(
                    "Ibuprofeno",
                    "200mg",
                    "Dolor de cabeza",
                    6,
                    LocalTime.of(8, 0),
                    LocalDateTime.of(2025, 9, 18, 8, 0),
                    null,
                    null
            );

            drugMapper.updateEntityFromRequest(sampleDrug, requestWithNullReminder);

            assertTrue(sampleDrug.getActiveReminder());
        }

        @Test
        @DisplayName("should update activeReminder to false when request activeReminder is false")
        void updateEntity_falseActiveReminder(){
            DrugRequest requestWithFalseReminder = new DrugRequest(
                    "Ibuprofeno",
                    "200mg",
                    "Dolor de cabeza",
                    6,
                    LocalTime.of(8, 0),
                    LocalDateTime.of(2025, 9, 18, 8, 0),
                    null,
                    false
            );

            drugMapper.updateEntityFromRequest(sampleDrug, requestWithFalseReminder);

            assertFalse(sampleDrug.getActiveReminder());
        }
    }
}
