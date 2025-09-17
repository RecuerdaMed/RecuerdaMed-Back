DELETE FROM drugs;

INSERT INTO drugs (
    id, drug_name, description, dosage, frequency_hours, next_intake_time,
    start_date, end_date, active, active_reminder, creation_date, update_date
) VALUES
(1, 'Paracetamol', 'Analgesico y antipiretico', '500mg', 8, '08:00:00',
 '2025-01-01 08:00:00', NULL, true, true, '2025-01-01 08:00:00', '2025-01-01 08:00:00'),

(2, 'Ibuprofeno', 'Antiinflamatorio', '400mg', 12, '12:00:00',
 '2025-01-01 12:00:00', NULL, true, true, '2025-01-01 12:00:00', '2025-01-01 12:00:00');
