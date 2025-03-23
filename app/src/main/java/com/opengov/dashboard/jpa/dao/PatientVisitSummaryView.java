package com.opengov.dashboard.jpa.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "patient_visit_summary")
public class PatientVisitSummaryView implements Serializable {
    @Id
    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private int age;

    @Column(name="gender")
    private String gender;

    @Column(name = "diagnosis_list")
    private String diagnosisList;

    @Column(name="total_visits")
    private int totalVisits;

    @Column(name = "last_visit_date")
    private LocalDate visitsLastDate;

    @Column(name = "visits_last_year")
    private int VisitsLastYear;

}
