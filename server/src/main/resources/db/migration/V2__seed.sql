-- ===== SEED PODACI =====

INSERT INTO service_package (name, description, duration_days, price) VALUES
                                                                          ('Monthly',  '30 days access', 30, 3000),
                                                                          ('Quarter',  '90 days access', 90, 8000),
                                                                          ('Annual',   '365 days access', 365, 25000);

INSERT INTO trainer (first_name, last_name, email, username, password_hash) VALUES
                                                                                ('Marko', 'Marković', 'marko@fit.rs', 'marko', '$2a$10$examplehash1'),
                                                                                ('Ana',   'Jovanović','ana@fit.rs',   'ana',   '$2a$10$examplehash2');

INSERT INTO certificate (name, type) VALUES
                                         ('ACE CPT', 'PT'),
                                         ('S&C Lv1', 'S&C');

INSERT INTO member (first_name, last_name, email, package_id) VALUES
                                                                  ('Petar', 'Petrović', 'petar@example.com', (SELECT id FROM service_package WHERE name='Monthly')),
                                                                  ('Ivana', 'Ivić',     'ivana@example.com', (SELECT id FROM service_package WHERE name='Quarter'));

INSERT INTO exercise (name, description, effort) VALUES
                                                     ('Bench Press', 'Compound upper-body press', 1.26),
                                                     ('Back Squat',  'Compound lower-body squat', 1.32),
                                                     ('Biceps Curl', 'Isolation elbow flexion',   1.05);

INSERT INTO trainer_certificate (trainer_id, certificate_id, issued_at) VALUES
                                                                            ((SELECT id FROM trainer WHERE username='marko'), (SELECT id FROM certificate WHERE name='ACE CPT'), '2023-01-10'),
                                                                            ((SELECT id FROM trainer WHERE username='ana'),   (SELECT id FROM certificate WHERE name='S&C Lv1'), '2024-03-05');

INSERT INTO training_record (training_date, intensity, trainer_id, member_id) VALUES
                                                                                  (CURDATE(), 0, (SELECT id FROM trainer WHERE username='marko'), (SELECT id FROM member WHERE email='petar@example.com')),
                                                                                  (CURDATE(), 0, (SELECT id FROM trainer WHERE username='ana'),   (SELECT id FROM member WHERE email='ivana@example.com'));

INSERT INTO training_record_item (record_id, rb, exercise_id, sets, reps, weight) VALUES
                                                                                      ((SELECT MIN(id) FROM training_record), 1, (SELECT id FROM exercise WHERE name='Bench Press'), 4, 8, 60.00),
                                                                                      ((SELECT MIN(id) FROM training_record), 2, (SELECT id FROM exercise WHERE name='Biceps Curl'), 3, 10, 20.00),
                                                                                      ((SELECT MAX(id) FROM training_record), 1, (SELECT id FROM exercise WHERE name='Back Squat'), 5, 5, 90.00);
