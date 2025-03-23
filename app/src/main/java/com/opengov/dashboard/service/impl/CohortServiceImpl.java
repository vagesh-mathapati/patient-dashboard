package com.opengov.dashboard.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opengov.dashboard.jpa.dao.CohortDao;
import com.opengov.dashboard.jpa.repository.CohortsRepo;
import com.opengov.dashboard.org.openapi.model.CohortRequest;
import com.opengov.dashboard.org.openapi.model.CohortResponse;
import com.opengov.dashboard.org.openapi.model.FilterObject;
import com.opengov.dashboard.org.openapi.model.PatientsResponse;
import com.opengov.dashboard.service.interfaces.CohortService;
import com.opengov.dashboard.service.interfaces.PatientService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class CohortServiceImpl implements CohortService {

    @Autowired
    CohortsRepo cohortsRepo;

    @Autowired
    ObjectMapper objectMapper ;

    @Autowired
    PatientService patientService;

    @Override
    @Transactional
    public CohortResponse createCohort(String hospitalId, CohortRequest cohortRequest) {

        CohortResponse cohortResponse = new CohortResponse();
        try {
            //Converter missing
            CohortDao cohortDao = new CohortDao();
            cohortDao.setId(UUID.randomUUID());
            cohortDao.setName(cohortRequest.getName());
            String cohortFilter = objectMapper.writeValueAsString(cohortRequest.getFilters());
            cohortDao.setFilter(cohortFilter);
            cohortDao.setCreatedAt(new Date());
            cohortDao.setUpdatedAt(new Date());
            cohortDao = cohortsRepo.save(cohortDao);

            cohortResponse.setId(cohortDao.getId().toString());
            cohortResponse.setName(cohortDao.getName());
            cohortResponse.setCreatedAt(cohortDao.getCreatedAt().toString());
            cohortResponse.setUpdatedAt(cohortDao.getUpdatedAt().toString());

            FilterObject filterObject = objectMapper.readValue(cohortDao.getFilter(), FilterObject.class);
            cohortResponse.setFilters(filterObject);
        }catch (Exception ex){
            log.error("Error while creating cohort", ex);
        }
        return cohortResponse;
    }

    @Override
    public CohortResponse getCohort(String hospitalId, String cohortId) {
        UUID uuid = UUID.fromString(cohortId);
        CohortDao cohortDao = cohortsRepo.findById(uuid).orElse(null);
        if(cohortDao != null){
            CohortResponse cohortResponse = new CohortResponse();
            cohortResponse.setId(cohortDao.getId().toString());
            cohortResponse.setName(cohortDao.getName());
            try {
                FilterObject filterObject = objectMapper.readValue(cohortDao.getFilter(), FilterObject.class);
                cohortResponse.setFilters(filterObject);

                cohortResponse.setCreatedAt(cohortDao.getCreatedAt().toString());
                cohortResponse.setUpdatedAt(cohortDao.getUpdatedAt().toString());
            }catch (Exception ex){
                log.error("Error while converting filter object", ex);
            }
            return cohortResponse;
        }
        return null;
    }

    @Override
    public PatientsResponse executeCohort(String hospitalId, String cohortId,  Integer pageNumber,
                                           Integer pageSize) {
       CohortResponse cohortResponse = getCohort(hospitalId, cohortId);
          if(cohortResponse != null){
            return patientService.getPatientsWithVisitDetails(hospitalId, cohortResponse.getFilters()
            ,pageNumber, pageSize);
          }
        return null;
    }

}
