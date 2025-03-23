package com.opengov.dashboard.converter.jpatoopenapimodel;

import com.opengov.dashboard.jpa.dao.PatientDao;
import com.opengov.dashboard.org.openapi.model.Patient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PatientDaoToPatient implements Converter<PatientDao, Patient> {

    @Override
    public Patient convert(PatientDao patientDao) {
        Patient patient = new Patient();
        patient.setId(patientDao.getId().toString());
        patient.setFirstName(patientDao.getFirstName());
        patient.setLastName(patientDao.getLastName());
        patient.setAge(patientDao.getAge());
        patient.setGender(patientDao.getGender().equals("Male")? Patient.GenderEnum.MALE :
                Patient.GenderEnum.FEMALE);
        return patient;
    }
}
