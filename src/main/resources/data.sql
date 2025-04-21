-- Insert initial data into the 'persons' table
INSERT INTO person(first_name, last_name, gender, birth_date, death_date, user_id) VALUES
('John', 'Doe', 'MALE', '1980-01-15', NULL, 1),
('Jane', 'Doe', 'FEMALE', '1983-03-22', NULL, 1),
('Albert', 'Doe', 'MALE', '1950-01-27', '2017-01-01', 1),
('Elisabeth', 'Doe', 'FEMALE', '1957-10-30', NULL, 1);

-- Insert initial data into the 'relationships' table
INSERT INTO relation(person1, person2, type, user_id) VALUES
(2, 3, 'SPOUSE', 1),
(2, 1, 'CHILD', 1),
(3, 1, 'CHILD', 1),
(4, 5, 'SPOUSE', 1),
(4, 2, 'CHILD', 1),
(5, 2, 'CHILD', 1);