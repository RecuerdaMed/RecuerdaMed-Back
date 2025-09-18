package com.hackaton.recuerdamed.drug.repository;

import com.hackaton.recuerdamed.drug.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {
    List<Drug> findByActiveTrueOrderByNextIntakeTimeAsc();
    Optional<Drug> findByIdAndActiveTrue(Long id);
    List<Drug> findByDrugNameContainingIgnoreCaseAndActiveTrue(String drugName);

    @Query("SELECT d FROM Drug d WHERE d.active = true AND d.activeReminder = true AND d.nextIntakeTime <= :currentTime")
    List<Drug> findDrugsForReminder(@Param("currentTime") LocalTime currentTime);
}
