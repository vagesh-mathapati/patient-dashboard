package com.opengov.dashboard.config;

import com.opengov.dashboard.converter.jpatoopenapimodel.PatientDaoToPatient;
import com.opengov.dashboard.converter.jpatoopenapimodel.PatientVisitSummaryViewToPatient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    PatientDaoToPatient patientDaoToPatient;

    @Autowired
    PatientVisitSummaryViewToPatient patientVisitSummaryViewToPatient;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(patientDaoToPatient);
        registry.addConverter(patientVisitSummaryViewToPatient);
    }
}
