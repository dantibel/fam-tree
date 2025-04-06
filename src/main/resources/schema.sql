-- DROP TYPE IF EXISTS GENDER CASCADE;
-- CREATE TYPE GENDER AS ENUM('MALE', 'FEMALE', 'OTHER');

DROP TABLE IF EXISTS Person CASCADE;
CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    gender CHAR(6) NOT NULL, -- TODO: use enum type
    portrait_url VARCHAR(255),
    birth_date DATE,
    death_date DATE
);

-- DROP TYPE IF EXISTS RELATION_TYPE CASCADE;
-- CREATE TYPE RELATION_TYPE AS ENUM('child', 'spouse');

DROP TABLE IF EXISTS Relation CASCADE;
CREATE TABLE relation (
    id SERIAL PRIMARY KEY,
    person1 INT NOT NULL,
    person2 INT NOT NULL,
    type CHAR(6) NOT NULL, -- TODO: use enum type
    FOREIGN KEY (person1) REFERENCES person(id),
    FOREIGN KEY (person2) REFERENCES person(id)
);

DROP TABLE IF EXISTS Photo CASCADE;
CREATE TABLE photo (
    id SERIAL PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    description TEXT,
    date DATE
);

DROP TABLE IF EXISTS Photo_Person CASCADE;
CREATE TABLE photo_person (
    id SERIAL PRIMARY KEY,
    photo INT NOT NULL,
    person INT NOT NULL,
    FOREIGN KEY (photo) REFERENCES photo(id),
    FOREIGN KEY (person) REFERENCES person(id)
);

DROP INDEX IF EXISTS idx_person1_id CASCADE;
CREATE INDEX idx_person1_id ON relation(person1);
DROP INDEX IF EXISTS idx_person2_id CASCADE;
CREATE INDEX idx_person2_id ON relation(person2);

DROP INDEX IF EXISTS idx_photo_id CASCADE;
CREATE INDEX idx_photo_id ON photo_person(photo);
DROP INDEX IF EXISTS idx_person_id CASCADE;
CREATE INDEX idx_person_id ON photo_person(person);