package com.opengov.dashboard.jpa.repository;

import com.opengov.dashboard.jpa.dao.CohortDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CohortsRepo extends JpaRepository<CohortDao, UUID> {
}
