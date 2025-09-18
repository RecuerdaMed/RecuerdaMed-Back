package com.hackaton.recuerdamed.drug.service;

import com.hackaton.recuerdamed.drug.dto.DrugMapper;
import com.hackaton.recuerdamed.drug.dto.DrugRequest;
import com.hackaton.recuerdamed.drug.dto.DrugResponse;
import com.hackaton.recuerdamed.drug.entity.Drug;
import com.hackaton.recuerdamed.drug.repository.DrugRepository;
import com.hackaton.recuerdamed.shared.custom_exception.DrugNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for DrugServiceImpl")
public class DrugServiceTest {
    @Mock
    private DrugRepository drugRepository;

    @Mock
    private DrugMapper drugMapper;

    @InjectMocks
    private DrugServiceImpl drugService;

    private Drug sampleDrug;
    private DrugResponse sampleDrugResponse;

    @BeforeEach
    void setUp(){

        sampleDrug = Drug.builder()
                .id(1L)
                .drugName("Ibuprofeno")
                .description("Para el dolor")
                .dosage("200mg")
                .frequencyHours(8)
                .nextIntakeTime(LocalTime.of(10, 0))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .active(true)
                .activeReminder(true)
                .build();

        sampleDrugResponse = new DrugResponse(
                sampleDrug.getId(),
                sampleDrug.getDrugName(),
                sampleDrug.getDescription(),
                sampleDrug.getDosage(),
                sampleDrug.getFrequencyHours(),
                sampleDrug.getNextIntakeTime(),
                sampleDrug.getStartDate(),
                sampleDrug.getEndDate(),
                sampleDrug.getActive(),
                sampleDrug.getActiveReminder(),
                sampleDrug.getCreationDate(),
                sampleDrug.getUpdateDate()
        );
    }

    @Nested
    @DisplayName("getAllDrugs")
    class GetAllDrugs{
        @Test
        @DisplayName("should return a list of drugs when drugs exist")
        void getAllDrugs_returnsListOfDrug(){
            when(drugRepository.findByActiveTrueOrderByNextIntakeTimeAsc())
                    .thenReturn(List.of(sampleDrug));
            when(drugMapper.toDto(sampleDrug)).thenReturn(sampleDrugResponse);

            List<DrugResponse> result = drugService.getAllDrugs();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Ibuprofeno", result.getFirst().drugName());
            verify(drugRepository, times(1)).findByActiveTrueOrderByNextIntakeTimeAsc();
            verify(drugMapper, times(1)).toDto(sampleDrug);
        }

        @Test
        @DisplayName("should return empty list when no drugs exist")
        void getAllDrugs_returnsEmptyList() {
            when(drugRepository.findByActiveTrueOrderByNextIntakeTimeAsc()).thenReturn(List.of());

            List<DrugResponse> result = drugService.getAllDrugs();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(drugRepository, times(1)).findByActiveTrueOrderByNextIntakeTimeAsc();
            verify(drugMapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("getDrugById")
    class GetDrugById{
        @Test
        @DisplayName("should return drug when id exists")
        void getDrugById_returnsDrug(){
            when(drugRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(sampleDrug));
            when(drugMapper.toDto(sampleDrug)).thenReturn(sampleDrugResponse);

            DrugResponse result = drugService.getDrugById(1L);

            assertNotNull(result);
            assertEquals("Ibuprofeno", result.drugName());
            verify(drugRepository, times(1)).findByIdAndActiveTrue(1L);
            verify(drugMapper, times(1)).toDto(sampleDrug);
        }

        @Test
        @DisplayName("should throw DrugNotFoundException when id does not exist")
        void getDrugById_throwsException_whenIdNotFound(){
            when(drugRepository.findByIdAndActiveTrue(99L)).thenReturn(Optional.empty());

            DrugNotFoundException exception = assertThrows(DrugNotFoundException.class,
                    () -> drugService.getDrugById(99L));

            assertEquals("Drug not found with ID:99", exception.getMessage());
            verify(drugRepository, times(1)).findByIdAndActiveTrue(99L);
            verify(drugMapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("updateDrug")
    class UpdateDrug {

        @Test
        @DisplayName("should update drug when drug exist")
        void updateDrug_shouldUpdateDrug_whenDrugExist() {
            DrugRequest request = new DrugRequest("Ibuprofeno modificado", "antiinflamatorio", "400 mg", 6, LocalTime.of(12,0), LocalDateTime.now(), LocalDateTime.now().plusDays(7), true);

            when(drugRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(sampleDrug));
            when(drugRepository.save(any(Drug.class))).thenReturn(sampleDrug);
            when(drugMapper.toDto(any(Drug.class))).thenReturn( new DrugResponse(1L, "Ibuprofeno modificado", "antiinflamatorio", "400 mg", 6, LocalTime.of(12,0), LocalDateTime.now(), LocalDateTime.now().plusDays(7), true, true, null, null));

            DrugResponse result = drugService.updateDrug(1L, request);

            assertNotNull(result);
            assertEquals("Ibuprofeno modificado", result.drugName());
            assertEquals("antiinflamatorio", result.description());
            assertEquals("400 mg", result.dosage());
            assertEquals(6, result.frequencyHours());
            assertEquals(LocalTime.of(12, 0), result.nextIntakeTime());
            assertTrue(result.activeReminder());

            verify(drugRepository, times(1)).findByIdAndActiveTrue(1L);
            verify(drugRepository, times(1)).save(any(Drug.class));
            verify(drugMapper).toDto(any());
        }

        @Test
        @DisplayName("should throw DrugNotFoundException when drug not found")
        void updateDrug_shouldThrowException_whenDrugNotFound() {
            DrugRequest request = new DrugRequest("Otro", "otra descripción", "600 mg", 12, LocalTime.of(12,0), LocalDateTime.now(), LocalDateTime.now().plusDays(3), false);

            when(drugRepository.findByIdAndActiveTrue(99L)).thenReturn(Optional.empty());

            DrugNotFoundException exception = assertThrows(DrugNotFoundException.class, ()-> drugService.updateDrug(99L, request));

            assertEquals("Drug with ID: 99 not found", exception.getMessage());

            verify(drugRepository, times(1)).findByIdAndActiveTrue(99L);
            verify(drugRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deleteDrug")
    class DeleteDrugTests {
        @Test
        @DisplayName("should set active to false and save drug when id exists")
        void deleteDrug_success(){
            when(drugRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(sampleDrug));

            drugService.deleteDrug(1L);

            assertFalse(sampleDrug.getActive());
            verify(drugRepository, times(1)).findByIdAndActiveTrue(1L);
            verify(drugRepository, times(1)).save(sampleDrug);
        }
        
        @Test
        @DisplayName("should throw DrugNotFoundException when id does not exist")
        void deleteDrug_notFound(){
            when(drugRepository.findByIdAndActiveTrue(99L)).thenReturn(Optional.empty());

            DrugNotFoundException exception = assertThrows(DrugNotFoundException.class,
                    () -> drugService.deleteDrug(99L)
            );

            assertEquals("Drug not found with ID: 99", exception.getMessage());
            verify(drugRepository, times(1)).findByIdAndActiveTrue(99L);
            verify(drugRepository, never()).save(any());
        }
    }
}