package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
    List<Booking> findByScreeningId(Long screeningId);

    List<Booking> findByUserId(Long userId);
}
