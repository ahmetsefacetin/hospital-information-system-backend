package com.cetin.hospital.repository;

import com.cetin.hospital.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {

    @Query("SELECT t FROM Time t WHERE t.doctor.id = :doctorId AND t.time >= :startDate AND t.time <= :endDate ORDER BY t.time ASC")
    List<Time> findTimesByDoctorId(@Param("doctorId") Long doctorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT MAX(t.time) FROM Time t WHERE t.doctor.id = :doctorId")
    LocalDateTime findLastClockByDoctorId(@Param("doctorId") Long doctorId);

    @Modifying
    @Query("DELETE FROM Time t WHERE t.time < :cutOffDate AND t.doctor.id = :doctorId")
    void deleteOldClocks(@Param("cutOffDate") LocalDateTime cutOffDate, @Param("doctorId") Long doctorId);

}
