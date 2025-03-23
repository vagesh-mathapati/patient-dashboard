package com.opengov.dashboard.service.impl;

import com.opengov.dashboard.jpa.dao.PatientDao;
import com.opengov.dashboard.jpa.dao.PatientVisitSummaryView;
import com.opengov.dashboard.jpa.repository.PatientVisitSummaryViewRepo;
import com.opengov.dashboard.jpa.specification.PatientVisitSpecification;
import com.opengov.dashboard.model.FilterCriteria;
import com.opengov.dashboard.org.openapi.model.*;
import com.opengov.dashboard.jpa.repository.PatientRepo;
import com.opengov.dashboard.service.interfaces.PatientService;
import com.opengov.dashboard.jpa.specification.PatientSpecification;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private PatientVisitSummaryViewRepo patientVisitSummaryViewRepo;

    @Autowired
    ConversionService conversionService;

    @Override
    public PatientsResponse getPatients(@NotNull String hospitalId,
                                        @NotNull FilterObject filterObject,
                                        @NotNull Integer pageNumber,
                                        @NotNull Integer pageSize) {
        PatientsResponse patientsResponse = new PatientsResponse();
        try {
            log.info("Getting patients for hospitalId: {}, filterObject: {}, pageNumber: {}, pageSize: {}",
                    hospitalId, filterObject, pageNumber, pageSize);
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            PatientSpecification specification = new PatientSpecification(convertToGenericFilterMap(filterObject));
            Page<PatientDao> patientDaoPage = patientRepo.findAll(specification, pageable);

            Pagination pagination = new Pagination();
            pagination.setPageNumber(pageNumber);
            pagination.setPageSize(pageSize);
            pagination.setTotal((int)patientDaoPage.getTotalElements());
            List<Patient> patientList = new ArrayList<>();
            patientDaoPage.forEach(patientDao -> {
                Patient patient = conversionService.convert(patientDao, Patient.class);
                patientList.add(patient);
            });

            patientsResponse.setPagination(pagination);
            patientsResponse.setPatients(patientList);
            Links links= new Links();
            links.setNext("http://localhost:8080/patients?hospitalId="+hospitalId+"&pageNumber="+(pageNumber+1)+"&pageSize="+pageSize);
            links.setPrev("http://localhost:8080/patients?hospitalId="+hospitalId+"&pageNumber="+(pageNumber-1)+"&pageSize="+pageSize);
            patientsResponse.setLinks(links);

            log.info("Found {} patients for hospitalId: {}, filterObject: {}, pageNumber: {}, pageSize: {}",
                    patientDaoPage.getTotalElements(), hospitalId, filterObject, pageNumber, pageSize);
        }catch (Exception e){
            log.error("Error while getting patients for hospitalId: {}, filterObject: {}, pageNumber: {}, pageSize: {}",
                    hospitalId, filterObject, pageNumber, pageSize, e);
        }
        return patientsResponse;
    }

    @Override
    public PatientsResponse getPatientsWithVisitDetails(String hospitalId, FilterObject filterObject, Integer pageNumber, Integer pageSize) {
        PatientsResponse patientsResponse = new PatientsResponse();
        try {
            log.info("Getting patients for hospitalId: {}, filterObject: {}, pageNumber: {}, pageSize: {}",
                    hospitalId, filterObject, pageNumber, pageSize);
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            PatientVisitSpecification specification = new PatientVisitSpecification(convertToGenericFilterMap(filterObject));
            Page<PatientVisitSummaryView> patientVisitSummaryViewPage = patientVisitSummaryViewRepo.findAll(specification, pageable);

            Pagination pagination = new Pagination();
            pagination.setPageNumber(pageNumber);
            pagination.setPageSize(pageSize);
            pagination.setTotal((int)patientVisitSummaryViewPage.getTotalElements());
            List<Patient> patientList = new ArrayList<>();
            patientVisitSummaryViewPage.forEach(patientVisitSummaryView -> {
                Patient patient = conversionService.convert(patientVisitSummaryView, Patient.class);
                patientList.add(patient);
            });

            patientsResponse.setPagination(pagination);
            patientsResponse.setPatients(patientList);
            Links links= new Links();
            links.setNext("http://localhost:8080/patients-details?hospitalId="+hospitalId+"&pageNumber="+(pageNumber+1)+"&pageSize="+pageSize);
            links.setPrev("http://localhost:8080/patients-details?hospitalId="+hospitalId+"&pageNumber="+(pageNumber-1)+"&pageSize="+pageSize);
            patientsResponse.setLinks(links);

            log.info("Found {} patients for hospitalId: {}, filterObject: {}, pageNumber: {}, pageSize: {}",
                    patientVisitSummaryViewPage.getTotalElements(), hospitalId, filterObject, pageNumber, pageSize);
        }catch (Exception e){
            log.error("Error while getting patients for hospitalId: {}, filterObject: {}, pageNumber: {}, pageSize: {}",
                    hospitalId, filterObject, pageNumber, pageSize, e);
        }
        return patientsResponse;
    }


    private Map<String, FilterCriteria> convertToGenericFilterMap(FilterObject filterObject) {
        Map<String, FilterCriteria> filterMap = new HashMap<>();

        if (filterObject.getAge() != null) {
            filterMap.put("age", new FilterCriteria(filterObject.getAge().getOperator(),
                    filterObject.getAge().getValue()));
        }
        if (filterObject.getDiagnosis() != null) {
            filterMap.put("diagnosis", new FilterCriteria(filterObject.getDiagnosis().getOperator().getValue(),
                    filterObject.getDiagnosis().getValue()));
        }
        if (filterObject.getDoctor() != null) {
            filterMap.put("doctor", new FilterCriteria(filterObject.getDoctor().getOperator().getValue(),
                    filterObject.getDoctor().getValue()));
        }
        if (filterObject.getLastVisitAfter() != null) {
            filterMap.put("lastVisitAfter", new FilterCriteria(filterObject.getLastVisitAfter().getOperator().getValue(),
                    filterObject.getLastVisitAfter().getValue()));
        }

        return filterMap;
    }
}
