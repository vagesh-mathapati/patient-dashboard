--liquibase formatted sql

--changeset opengov:healthcaredb-1
CREATE TABLE IF NOT EXISTS patients (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    age INT,
    gender VARCHAR(10),
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);

--changeset opengov:healthcaredb-2
CREATE TABLE IF NOT EXISTS doctors (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    department VARCHAR(100),
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL);


-- changeset opengov:healthcaredb-3
CREATE TABLE IF NOT EXISTS visits (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id UUID NOT NULL,
    doctor_id UUID NOT NULL,
    visit_date DATE NOT NULL DEFAULT NOW(),
    diagnosis TEXT,
    prescription TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_patient FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE,
    CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id) ON DELETE CASCADE
);

--changeset opengov:healthcaredb-4

CREATE OR REPLACE VIEW patient_visit_summary AS
SELECT
    p.id AS patient_id,
    p.first_name,
    p.last_name,
    p.age,
    p.gender,
    STRING_AGG(v.diagnosis, ', ') AS diagnosis_list,
    d.first_name || ' ' || d.last_name AS doctor_name,
    MAX(v.visit_date) AS last_visit_date,
    COUNT(v.id) AS total_visits,
    COUNT(CASE WHEN v.visit_date >= CURRENT_DATE - INTERVAL '1 year' THEN 1 END) AS visits_last_year
FROM patients p
LEFT JOIN visits v ON p.id = v.patient_id
LEFT JOIN doctors d ON v.doctor_id = d.id
GROUP BY p.id, d.id

-- changeset opengov:healthcaredb-5
CREATE TABLE IF NOT EXISTS cohorts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100),
    filter TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);