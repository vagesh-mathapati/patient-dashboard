package com.opengov.dashboard.jpa.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "visits")
@Getter
@Setter
public class VisitsDao extends AuditModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;

  @Column(name = "patient_id", nullable = false)
  private String patientId;

  @Column(name = "doctor_id", nullable = false)
  private String doctorId;

  @Column(name = "visit_date", nullable = false)
  private Date visitDate;

  @Column(name = "diagnosis", nullable = false)
  private String diagnosis;

  @Column(name = "prescription", nullable = false)
  private String prescription;

  @Column(name = "notes")
  private String notes;


}
