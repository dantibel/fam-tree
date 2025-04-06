-- Insert initial data into the 'persons' table
INSERT INTO person(first_name, last_name, gender, birth_date) VALUES
('John', 'Doe', 'MALE', '1980-01-15'),
('Jane', 'Doe', 'FEMALE', '1983-03-22'),
('Alice', 'Doe', 'FEMALE', '2007-07-10'),
('Bob', 'Doe', 'MALE', '2012-11-05'),
('Albert', 'Doe', 'MALE', '1950-01-27'),
('Elisabeth', 'Doe', 'FEMALE', '1939-10-30');

-- Insert initial data into the 'relationships' table
INSERT INTO relation(person1, person2, type) VALUES
(1, 2, 'SPOUSE'),
(1, 3, 'CHILD'),
(1, 4, 'CHILD'),
(2, 3, 'CHILD'),
(2, 4, 'CHILD'),
(5, 6, 'SPOUSE'),
(5, 1, 'CHILD'),
(6, 1, 'CHILD');