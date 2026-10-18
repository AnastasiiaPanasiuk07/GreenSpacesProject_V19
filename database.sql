DROP DATABASE IF EXISTS green_spaces_db;
CREATE DATABASE green_spaces_db;
USE green_spaces_db;

CREATE TABLE tree (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      species VARCHAR(160) NOT NULL,
                      location VARCHAR(200) NOT NULL,
                      planting_date INT NOT NULL,
                      condition_score INT NOT NULL
);

CREATE TABLE inspection (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            tree_id BIGINT NOT NULL,
                            inspection_date VARCHAR(10) NOT NULL,
                            f1 DOUBLE NOT NULL,
                            f2 DOUBLE NOT NULL,
                            f3 DOUBLE NOT NULL,
                            risk_index DOUBLE NOT NULL,
                            FOREIGN KEY (tree_id) REFERENCES tree(id)
);

CREATE TABLE work_order_request (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    inspection_id BIGINT NOT NULL,
                                    state VARCHAR(30) NOT NULL,
                                    description VARCHAR(500) NOT NULL,
                                    FOREIGN KEY (inspection_id) REFERENCES inspection(id)
);

CREATE TABLE crew_assignment (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 request_id BIGINT NOT NULL,
                                 crew_name VARCHAR(120) NOT NULL,
                                 description VARCHAR(500) NOT NULL,
                                 FOREIGN KEY (request_id) REFERENCES work_order_request(id)
);

INSERT INTO tree (species, location, planting_date, condition_score) VALUES
                                                                         ('Дуб звичайний', 'вул. Соборна, 12', '2010-05-15', 85.0),
                                                                         ('Липа серцелиста', 'парк Шевченка', '2015-04-20', 70.0),
                                                                         ('Каштан кінський', 'вул. Європейська, 22', '2012-09-10', 55.0);

INSERT INTO inspection (tree_id, inspection_date, f1, f2, f3, risk_index) VALUES
                                                                              (1, '2026-10-01', 40.0, 50.0, 60.0, 50.0),
                                                                              (2, '2026-10-02', 70.0, 80.0, 90.0, 78.0),
                                                                              (3, '2026-10-03', 85.0, 90.0, 95.0, 89.0);

INSERT INTO work_order_request (inspection_id, state, description) VALUES
                                                                       (1, 'CREATED', 'Плановий догляд та санітарна обрізка крони'),
                                                                       (2, 'CHECKED', 'Усунення пошкоджень гілок після негоди'),
                                                                       (3, 'PLANNED', 'Термінова зрубка аварійного дерева');

INSERT INTO crew_assignment (request_id, crew_name, description) VALUES
                                                                      (1, 'Бригада №1 (Зеленбуд)', 'Виконати обрізку згідно з планом'),
                                                                      (2, 'Бригада №2 (Ремонтники)', 'Ліквідація наслідків негоди'),
                                                                      (3, 'Бригада №3 (Аварійна)', 'Термінові заходи безпеки');