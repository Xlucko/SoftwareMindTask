package com.softwaremind.task.repository;

import com.softwaremind.task.model.RequestCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RequestCountRepository extends JpaRepository<RequestCount, Long>, JpaSpecificationExecutor<RequestCount> {

    Optional<RequestCount> findByMethodAndPath(String method, String path);
}
