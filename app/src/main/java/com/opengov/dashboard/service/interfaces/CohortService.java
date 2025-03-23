package com.opengov.dashboard.service.interfaces;

import com.opengov.dashboard.org.openapi.model.CohortRequest;
import com.opengov.dashboard.org.openapi.model.CohortResponse;
import com.opengov.dashboard.org.openapi.model.PatientsResponse;
import jakarta.validation.constraints.NotNull;

public interface CohortService {
   CohortResponse createCohort(@NotNull String hospitalId,@NotNull CohortRequest cohortRequest);
   CohortResponse getCohort(@NotNull String hospitalId,@NotNull String cohortId);

   PatientsResponse executeCohort(@NotNull String hospitalId,@NotNull String cohortId,
                                  @NotNull Integer pageNumber,
                                  @NotNull Integer pageSize);
}
