package com.opengov.dashboard.controller;

import com.opengov.dashboard.model.FilterCriteria;
import com.opengov.dashboard.org.openapi.PatientDetailsApi;
import com.opengov.dashboard.org.openapi.model.FilterObject;
import com.opengov.dashboard.org.openapi.model.PatientsResponse;
import com.opengov.dashboard.service.interfaces.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class PatientDashboardController implements PatientDetailsApi {

    @Autowired
    PatientService patientService;

    @Override
    public ResponseEntity<PatientsResponse> getPatients(String hospitalId, FilterObject filterObject, Integer pageNumber,
                                                        Integer pageSize) {
        return  ResponseEntity.ok(patientService.getPatients(hospitalId, filterObject, pageNumber, pageSize));
    }

    @Override
    public ResponseEntity<PatientsResponse> getPatientsDetails(String hospitalId, FilterObject filterObject, Integer pageNumber, Integer pageSize) {
        return  ResponseEntity.ok(patientService.getPatientsWithVisitDetails(hospitalId, filterObject, pageNumber, pageSize));
    }


}
