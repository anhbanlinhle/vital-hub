package com.main.server.repository;

import com.main.server.entity.Bicycling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BicyclingRepository extends JpaRepository<Bicycling, Long> {
}
