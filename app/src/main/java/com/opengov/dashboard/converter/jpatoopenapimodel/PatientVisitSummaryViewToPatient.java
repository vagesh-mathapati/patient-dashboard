package com.opengov.dashboard.converter.jpatoopenapimodel;

import com.opengov.dashboard.jpa.dao.PatientVisitSummaryView;
import com.opengov.dashboard.org.openapi.model.Patient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Arrays;

@Component
public class PatientVisitSummaryViewToPatient implements Converter<PatientVisitSummaryView, Patient> {

    @Override
    public Patient convert(PatientVisitSummaryView patientVisitSummaryView) {
        Patient patient = new Patient();
        patient.setId(patientVisitSummaryView.getPatientId());
        patient.setFirstName(patientVisitSummaryView.getFirstName());
        patient.setLastName(patientVisitSummaryView.getLastName());
        patient.setAge(patientVisitSummaryView.getAge());
        patient.setGender(patientVisitSummaryView.getGender().equals("Male")? Patient.GenderEnum.MALE :
                        Patient.GenderEnum.FEMALE);
        if(patientVisitSummaryView.getDiagnosisList()!=null) {
            patient.setDiagnosis(Arrays.stream(patientVisitSummaryView.getDiagnosisList().split(",")).toList());
        }
        patient.setVisitCount(patientVisitSummaryView.getTotalVisits());
        patient.setLastVisitDate(patientVisitSummaryView.getVisitsLastDate());
        patient.setVisitCountLastYear(patientVisitSummaryView.getVisitsLastYear());
        return patient;
    }
}
