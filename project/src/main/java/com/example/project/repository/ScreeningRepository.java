package com.example.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Screening;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
	List<Screening> findByMovieIdAndTheaterIdAndScreenTimeBetween(
	        Long movieId, Long theaterId, LocalDateTime startDateTime, LocalDateTime endDateTime);
	
}
