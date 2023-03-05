CREATE TABLE services (
 service_id SERIAL PRIMARY KEY,
 name VARCHAR(255) NOT NULL,
 price INT NOT NULL
 );

CREATE TABLE masters (
 master_id SERIAL PRIMARY KEY,
 name VARCHAR(255) NOT NULL
 );

CREATE TABLE skills (
 id SERIAL PRIMARY KEY,
 master_id INT NOT NULL,
 service_id INT NOT NULL,
 FOREIGN KEY (master_id) REFERENCES masters (master_id) ON DELETE 
CASCADE ON UPDATE CASCADE,
 FOREIGN KEY (service_id) REFERENCES services (service_id) ON DELETE 
CASCADE ON UPDATE CASCADE
 );

CREATE TABLE owners (
 owner_id SERIAL PRIMARY KEY,
 name VARCHAR(255) NOT NULL
 );

CREATE TABLE cars (
 car_id SERIAL PRIMARY KEY,
 owner_id INT NOT NULL,
 plate VARCHAR(255) NOT NULL,
 in_service BOOLEAN DEFAULT TRUE,
 FOREIGN KEY (owner_id) REFERENCES owners (owner_id) ON DELETE 
CASCADE ON UPDATE CASCADE
 );

CREATE TABLE tasks (
 task_id SERIAL PRIMARY KEY,
 car_id INT NOT NULL,
 service_id INT NOT NULL,
 master_id INT NOT NULL,
 date DATE,
 FOREIGN KEY (car_id) REFERENCES cars (car_id) ON DELETE CASCADE 
ON UPDATE CASCADE,
 FOREIGN KEY (service_id) REFERENCES services (service_id) ON DELETE 
CASCADE ON UPDATE CASCADE,
 FOREIGN KEY (master_id) REFERENCES masters (master_id) ON DELETE 
CASCADE ON UPDATE CASCADE
 );

INSERT INTO services (name, price) VALUES
 ('Замена масла', 100),
 ('Замена шин', 2000),
 ('Ремонт тормозов', 3000),
 ('Полная покраска', 4000),
 ('Мойка', 500),
 ('Химчистка', 1000),
 ('Тонирование', 1500);
 
INSERT INTO masters (name) VALUES
 ('Денис Медведев'),
 ('Дмитрий Трухман'),
 ('Глеб Лях');

INSERT INTO skills (master_id, service_id) VALUES
 (1, 1),
 (1, 2),
 (1, 3),
 (2, 1),
 (2, 2),
 (2, 3),
 (2, 4),
 (3, 1),
 (3, 2),
 (3, 3),
 (3, 4),
 (3, 5),
 (3, 6),
 (3, 7);

INSERT INTO owners (name) VALUES
 ('Иванов Иван Иванович'),
 ('Петров Петр Петрович'),
 ('Сидоров Сидр Сидорович');

INSERT INTO cars (owner_id, plate) VALUES
 (1, 'A111AA'),
 (1, 'A222AA'),
 (2, 'B111BB'),
 (2, 'B222BB'),
 (3, 'C111CC'),
 (3, 'C222CC');

INSERT INTO tasks (car_id, service_id, master_id, date) VALUES
 (1, 1, 1, '2019-01-01'),
 (1, 2, 1, '2019-01-02'),
 (1, 3, 1, '2019-01-03'),
 (2, 1, 1, '2019-01-04'),
 (2, 2, 1, '2019-01-05'),
 (2, 3, 1, '2019-01-06'),
 (3, 1, 2, '2019-01-07'),
 (3, 2, 2, '2019-01-08'),
 (3, 3, 2, '2019-01-09'),
 (3, 4, 2, '2019-01-10'),
 (4, 1, 2, '2019-01-11'),
 (4, 2, 2, '2019-01-12'),
 (4, 3, 2, '2019-01-13'),
 (4, 4, 2, '2019-01-14'),
 (5, 1, 3, '2019-01-15'),
 (5, 2, 3, '2019-01-16'),
 (5, 3, 3, '2019-01-17'),
 (5, 4, 3, '2019-01-18'),
 (5, 5, 3, '2019-01-19'),
 (5, 6, 3, '2019-01-20'),
 (5, 7, 3, '2019-01-21'),
 (6, 1, 3, '2019-01-22'),
 (6, 2, 3, '2019-01-23'),
 (6, 3, 3, '2019-01-24'),
 (6, 4, 3, '2019-01-25'),
 (6, 5, 3, '2019-01-26');

SELECT name AS Услуга, price AS Цена FROM services;

SELECT c.plate AS Номер, o.name AS Владелец FROM cars c
INNER JOIN owners o ON c.owner_id = o.owner_id
WHERE c.in_service = TRUE;

SELECT t.date AS Дата, s.name AS Услуга, m.name AS Мастер FROM tasks t
INNER JOIN services s ON t.service_id = s.service_id
INNER JOIN masters m ON t.master_id = m.master_id
WHERE t.car_id = (SELECT car_id FROM cars WHERE plate = 'A111AA');

SELECT m.name AS Мастер, s.name AS Специализация FROM masters m
INNER JOIN skills sk ON m.master_id = sk.master_id
INNER JOIN services s ON sk.service_id = s.service_id;

SELECT t.date AS Дата, s.name AS Услуга, c.plate AS Номер FROM tasks t
INNER JOIN services s ON t.service_id = s.service_id
INNER JOIN cars c ON t.car_id = c.car_id
WHERE t.master_id = (SELECT master_id FROM masters WHERE name = 'Глеб 
Лях')
AND t.date BETWEEN '2019-01-24' AND '2019-01-31';

SELECT SUM(s.price) AS Сумма FROM tasks t
INNER JOIN services s ON t.service_id = s.service_id
INNER JOIN cars c ON t.car_id = c.car_id
WHERE c.owner_id = (SELECT owner_id FROM owners WHERE name = 'Иванов 
Иван Иванович')
AND c.in_service = TRUE