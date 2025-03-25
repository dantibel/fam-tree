-- Insert initial data into the 'persons' table
INSERT INTO person(id, first_name, last_name, gender, birth_date) VALUES
(1, 'John', 'Doe', 'male', '1980-01-15'),
(2, 'Jane', 'Doe', 'female', '1982-03-22'),
(3, 'Alice', 'Doe', 'female', '1990-07-10'),
(4, 'Bob', 'Doe', 'male', '1988-11-05');

-- Insert initial data into the 'relationships' table
INSERT INTO relation(id, person1, person2, type) VALUES
(1, 1, 2, 'spouse'),
(3, 1, 3, 'child'),
(4, 1, 4, 'child');