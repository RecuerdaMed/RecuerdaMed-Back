package com.hackaton.recuerdamed.drug.repository;

import com.hackaton.recuerdamed.drug.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {
    List<Drug> findByActiveTrueOrderByNextIntakeTimeAsc();
    Optional<Drug> findByIdAndActiveTrue(Long id);
    List<Drug> findByDrugNameContainingIgnoreCaseAndActiveTrue(String drugName);

    @Query("SELECT m FROM Drug m WHERE m.active = true AND m.reminderActive = true AND m.nextIntake <= :currentTime")
    List<Drug> findDrugsForReminder(LocalTime currentTime);
}
