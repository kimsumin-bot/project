package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Theater;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long>{
	java.util.List<Theater> findByLocation(String location);
}
