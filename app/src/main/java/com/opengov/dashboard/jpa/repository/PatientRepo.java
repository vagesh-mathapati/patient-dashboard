package com.opengov.dashboard.jpa.repository;

import com.opengov.dashboard.jpa.dao.PatientDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepo extends JpaRepository<PatientDao, UUID>,
        JpaSpecificationExecutor<PatientDao> {

    //Page<PatientDao> findAll(PatientSpecification specification, Pageable pageable);
}
