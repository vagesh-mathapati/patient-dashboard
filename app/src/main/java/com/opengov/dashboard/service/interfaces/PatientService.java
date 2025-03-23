package com.opengov.dashboard.service.interfaces;

import com.opengov.dashboard.org.openapi.model.FilterObject;
import com.opengov.dashboard.org.openapi.model.PatientsResponse;
import jakarta.validation.constraints.NotNull;

public interface PatientService {

    //Ideally separate model should be created for service layer so that changes in openAPI
    // models or internal model will not impact each other
     PatientsResponse getPatients(@NotNull String hospitalId,
                                  @NotNull FilterObject filterObject,
                                  @NotNull Integer pageNumber,
                                  @NotNull Integer pageSize);


    PatientsResponse getPatientsWithVisitDetails(@NotNull String hospitalId,
                                 @NotNull FilterObject filterObject,
                                 @NotNull Integer pageNumber,
                                 @NotNull Integer pageSize);
}
