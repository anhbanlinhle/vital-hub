package com.main.server.repository;

import com.main.server.entity.Running;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningRepository extends JpaRepository<Running, Long> {
}
