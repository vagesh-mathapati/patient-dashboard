-- Insert 2000 Patients
INSERT INTO patients (id, first_name, last_name, age, gender, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    ('{John,Jane,Michael,Sarah,David,Emily,James,Emma,Daniel,Olivia,John1,Jane1,Michael1,Sarah1,David1,Emily1,James1,Emma1,Daniel1,Olivia1,Vagesh,Vagesh1}'::text[])[floor(random() * 22 + 1)],
    ('{Smith,Johnson,Brown,Williams,Jones,Miller,Davis,Wilson,Anderson,Thomas,Smith1,Johnson1,Brown1,Williams1,Jones1,Miller1,Davis1,Wilson1,Anderson1,Thomas1,Mathapati,Mathapati1}'::text[])[floor(random() * 22 + 1)],
    floor(random() * 60 + 20),  -- Age between 20 and 80
    ('{Male,Female}'::text[])[floor(random() * 2 + 1)],
    NOW(),
    NOW()
FROM generate_series(1, 2000);

-- Insert 50 Doctors
INSERT INTO doctors (id, first_name, last_name, department, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    ('{Alice,Bob,Charlie,David,Eve,Frank,Grace,Hannah,Isaac,Julia}'::text[])[floor(random() * 10 + 1)],
    ('{Taylor,Martinez,Anderson,Thomas,Moore,Jackson,White,Harris,Martin,Lee}'::text[])[floor(random() * 10 + 1)],
    ('{Cardiology,Neurology,Orthopedics,Oncology,Pediatrics,Dermatology,Urology,Endocrinology}'::text[])[floor(random() * 8 + 1)],
    NOW(),
    NOW()
FROM generate_series(1, 50);


-- Insert 10,000 Visits with Better Patient Distribution
WITH random_patients AS (
    SELECT id, ROW_NUMBER() OVER () AS rn FROM patients ORDER BY random()
),
random_doctors AS (
    SELECT id, ROW_NUMBER() OVER () AS rn FROM doctors ORDER BY random()
)
INSERT INTO visits (id, patient_id, doctor_id, visit_date, diagnosis, prescription, notes, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    p.id, 
    d.id,
    NOW() - INTERVAL '1 day' * floor(random() * 365),  -- Random visit date within last year
    ('{Hypertension,Diabetes,Flu,Asthma,Back Pain,Allergy,Arthritis,Depression,Infection}'::text[])[floor(random() * 9 + 1)],
    ('{Paracetamol,Ibuprofen,Metformin,Amoxicillin,Lisinopril,Atorvastatin,Omeprazole,Losartan}'::text[])[floor(random() * 8 + 1)],
    ('{Follow-up required,Healthy lifestyle suggested,Referred to specialist,Monitor symptoms,Prescribed medication}'::text[])[floor(random() * 5 + 1)],
    NOW(),
    NOW()
FROM generate_series(1, 10000) gs
JOIN random_patients p ON (gs % (SELECT COUNT(*) FROM patients)) + 1 = p.rn
JOIN random_doctors d ON (gs % (SELECT COUNT(*) FROM doctors)) + 1 = d.rn;
