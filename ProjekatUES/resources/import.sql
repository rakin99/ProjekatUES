-- Skripta koja se pokrece automatski pri pokretanju aplikacije
-- Baza koja se koristi je H2 in memory baza
-- Gasenjem aplikacije, brisu se svi podaci

-- Obe lozinke su hesovane pomocu BCrypt algoritma https://www.dailycred.com/article/bcrypt-calculator

INSERT INTO USERS (id, email, password, certificate, active, last_password_reset_date) VALUES (1, 'pantelijacappone@gmail.com', '$2a$10$XXh8WZEjVasyYiD80QHwt.xO.PR7ktfNrKj3/mQVmLEv.lz0AKDqu', './data/pantelijacappone@gmail.com.cer' ,true, '2017-10-01 21:58:58.508-07');
INSERT INTO USERS (id, email, password, certificate, active, last_password_reset_date) VALUES (2, 'rakindejan@gmail.com', '$2a$10$XXh8WZEjVasyYiD80QHwt.xO.PR7ktfNrKj3/mQVmLEv.lz0AKDqu', './data/Pera_Peric.cer' ,true, '2017-10-01 18:57:58.508-07');

INSERT INTO AUTHORITY (id, name) VALUES (1, 'ROLE_REGULAR');
INSERT INTO AUTHORITY (id, name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (2, 2);
