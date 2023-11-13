package com.main.server.repository;

import com.main.server.entity.PushUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushUpRepository extends JpaRepository<PushUp, Long> {
}
