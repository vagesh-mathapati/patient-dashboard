package com.opengov.dashboard.service.interfaces;

import com.opengov.dashboard.org.openapi.model.CohortRequest;
import com.opengov.dashboard.org.openapi.model.CohortResponse;
import com.opengov.dashboard.org.openapi.model.PatientsResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface CohortService {
   CohortResponse createCohort(@NotNull String hospitalId,@NotNull CohortRequest cohortRequest);
   CohortResponse getCohort(@NotNull String hospitalId,@NotNull String cohortId);

   PatientsResponse executeCohort(@NotNull String hospitalId,@NotNull String cohortId,
                                  @NotNull Integer pageNumber,
                                  @NotNull Integer pageSize);
   List<CohortResponse> getCohorts(@NotNull String hospitalId);
}
