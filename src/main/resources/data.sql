-- Insert initial data into the 'persons' table
INSERT INTO person(id, first_name, last_name, gender, birth_date) VALUES
(1, 'John', 'Doe', 'male', '1980-01-15'),
(2, 'Jane', 'Doe', 'female', '1983-03-22'),
(3, 'Alice', 'Doe', 'female', '2007-07-10'),
(4, 'Bob', 'Doe', 'male', '2012-11-05'),
(5, 'Albert', 'Doe', 'male', '1950-01-27'),
(6, 'Elisabeth', 'Doe', 'female', '1939-10-30');

-- Insert initial data into the 'relationships' table
INSERT INTO relation(id, person1, person2, type) VALUES
(1, 1, 2, 'spouse'),
(3, 1, 3, 'child'),
(4, 1, 4, 'child'),
(5, 2, 3, 'child'),
(6, 2, 4, 'child'),
(7, 5, 6, 'spouse'),
(8, 5, 1, 'child'),
(9, 6, 1, 'child');