package com.opengov.dashboard.controller;

import com.opengov.dashboard.org.openapi.CohortsApi;
import com.opengov.dashboard.org.openapi.model.CohortRequest;
import com.opengov.dashboard.org.openapi.model.CohortResponse;
import com.opengov.dashboard.org.openapi.model.PatientsResponse;
import com.opengov.dashboard.service.interfaces.CohortService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CohortController implements CohortsApi {

    @Autowired
    CohortService cohortService;

    @Override
    public ResponseEntity<CohortResponse> createCohort(String hospitalId, CohortRequest cohortRequest) {
        return ResponseEntity.created(null).body(cohortService.createCohort(hospitalId, cohortRequest));
    }

    @Override
    public ResponseEntity<Void> deleteCohort(String hospitalId, String cohortId) {
        return CohortsApi.super.deleteCohort(hospitalId, cohortId);
    }

    @Override
    public ResponseEntity<PatientsResponse> executeCohort(String hospitalId, String cohortId, Integer pageNumber, Integer pageSize) {
        return ResponseEntity.ok(cohortService.executeCohort(hospitalId, cohortId, pageNumber, pageSize));
    }

    @Override
    public ResponseEntity<CohortResponse> getCohortDetails(String hospitalId, String cohortId) {
        return ResponseEntity.ok(cohortService.getCohort(hospitalId, cohortId));
    }

    @Override
    public ResponseEntity<List<CohortResponse>> getCohorts(String hospitalId) {
        return CohortsApi.super.getCohorts(hospitalId);
    }

    @Override
    public ResponseEntity<CohortResponse> updateCohort(String hospitalId, String cohortId, CohortRequest cohortRequest) {
        return CohortsApi.super.updateCohort(hospitalId, cohortId, cohortRequest);
    }
}
