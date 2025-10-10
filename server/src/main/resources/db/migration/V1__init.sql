-- ===== SCHEMA: GymTracker (MySQL) =====

CREATE TABLE service_package (
                                 id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 name          VARCHAR(80)   NOT NULL,
                                 description   VARCHAR(255),
                                 duration_days INT           NOT NULL,
                                 price         INT           NOT NULL,
                                 UNIQUE KEY uq_package_name (name)
) ENGINE=InnoDB;

CREATE TABLE trainer (
                         id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                         first_name    VARCHAR(60)   NOT NULL,
                         last_name     VARCHAR(60)   NOT NULL,
                         email         VARCHAR(120),
                         username      VARCHAR(60)   NOT NULL,
                         password_hash VARCHAR(255),
                         UNIQUE KEY uq_trainer_username (username),
                         UNIQUE KEY uq_trainer_email (email)
) ENGINE=InnoDB;

CREATE TABLE certificate (
                             id    BIGINT PRIMARY KEY AUTO_INCREMENT,
                             name  VARCHAR(120) NOT NULL,
                             type  VARCHAR(80)  NOT NULL,
                             UNIQUE KEY uq_certificate_name (name)
) ENGINE=InnoDB;

CREATE TABLE member (
                        id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                        first_name  VARCHAR(60)   NOT NULL,
                        last_name   VARCHAR(60)   NOT NULL,
                        email       VARCHAR(120),
                        package_id  BIGINT,
                        CONSTRAINT fk_member_package
                            FOREIGN KEY (package_id) REFERENCES service_package(id)
                                ON UPDATE CASCADE ON DELETE SET NULL,
                        UNIQUE KEY uq_member_email (email),
                        KEY idx_member_name (last_name, first_name)
) ENGINE=InnoDB;

CREATE TABLE exercise (
                          id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name        VARCHAR(80)   NOT NULL,
                          description VARCHAR(255),
                          effort      DECIMAL(5,2)  NOT NULL DEFAULT 1.00,
                          UNIQUE KEY uq_exercise_name (name)
) ENGINE=InnoDB;

CREATE TABLE training_record (
                                 id             BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 training_date  DATE           NOT NULL,
                                 intensity      DECIMAL(18,2)  NOT NULL DEFAULT 0,
                                 trainer_id     BIGINT         NOT NULL,
                                 member_id      BIGINT         NOT NULL,
                                 CONSTRAINT fk_record_trainer FOREIGN KEY (trainer_id) REFERENCES trainer(id)
                                     ON UPDATE CASCADE ON DELETE RESTRICT,
                                 CONSTRAINT fk_record_member  FOREIGN KEY (member_id)  REFERENCES member(id)
                                     ON UPDATE CASCADE ON DELETE RESTRICT,
                                 KEY idx_record_date (training_date),
                                 KEY idx_record_trainer (trainer_id),
                                 KEY idx_record_member (member_id)
) ENGINE=InnoDB;

CREATE TABLE training_record_item (
                                      record_id   BIGINT  NOT NULL,
                                      rb          INT     NOT NULL,
                                      exercise_id BIGINT  NOT NULL,
                                      sets        INT     NOT NULL,
                                      reps        INT     NOT NULL,
                                      weight      DECIMAL(10,2) NOT NULL,
                                      PRIMARY KEY (record_id, rb),
                                      CONSTRAINT fk_item_record   FOREIGN KEY (record_id)   REFERENCES training_record(id)
                                          ON UPDATE CASCADE ON DELETE CASCADE,
                                      CONSTRAINT fk_item_exercise FOREIGN KEY (exercise_id) REFERENCES exercise(id)
                                          ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;


CREATE TABLE trainer_certificate (
                                     trainer_id     BIGINT NOT NULL,
                                     certificate_id BIGINT NOT NULL,
                                     issued_at      DATE   NOT NULL,
                                     PRIMARY KEY (trainer_id, certificate_id),
                                     CONSTRAINT fk_tc_trainer     FOREIGN KEY (trainer_id)     REFERENCES trainer(id)
                                         ON UPDATE CASCADE ON DELETE CASCADE,
                                     CONSTRAINT fk_tc_certificate FOREIGN KEY (certificate_id) REFERENCES certificate(id)
                                         ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

