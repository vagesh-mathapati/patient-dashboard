package com.opengov.dashboard.jpa.repository;

import com.opengov.dashboard.jpa.dao.PatientDao;
import com.opengov.dashboard.jpa.dao.PatientVisitSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientVisitSummaryViewRepo extends JpaRepository<PatientVisitSummaryView, String>,
        JpaSpecificationExecutor<PatientVisitSummaryView> {

}
