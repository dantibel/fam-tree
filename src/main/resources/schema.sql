-- DROP TYPE IF EXISTS GENDER CASCADE;
-- CREATE TYPE GENDER AS ENUM('MALE', 'FEMALE', 'OTHER');

DROP TABLE IF EXISTS person CASCADE;
CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    gender CHAR(6) NOT NULL, -- TODO: use enum type
    portrait_url VARCHAR(255),
    birth_date DATE,
    death_date DATE,

    user_id INT,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);

-- DROP TYPE IF EXISTS RELATION_TYPE CASCADE;
-- CREATE TYPE RELATION_TYPE AS ENUM('child', 'spouse');

DROP TABLE IF EXISTS relation CASCADE;
CREATE TABLE relation (
    id SERIAL PRIMARY KEY,
    person1 INT NOT NULL,
    person2 INT NOT NULL,
    type CHAR(6) NOT NULL, -- TODO: use enum type
    FOREIGN KEY (person1) REFERENCES person(id),
    FOREIGN KEY (person2) REFERENCES person(id),

    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);

DROP TABLE IF EXISTS photo CASCADE;
CREATE TABLE photo (
    id SERIAL PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    description TEXT,
    date DATE,

    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);

DROP TABLE IF EXISTS photo_person CASCADE;
CREATE TABLE photo_person (
    id SERIAL PRIMARY KEY,
    photo INT NOT NULL,
    person INT NOT NULL,
    FOREIGN KEY (photo) REFERENCES photo(id),
    FOREIGN KEY (person) REFERENCES person(id),

    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);

DROP TABLE IF EXISTS app_user CASCADE;
CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_role VARCHAR(10) NOT NULL DEFAULT 'USER',
    person INT,
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


-- Restrict access to rows in the tables based on the logged user's ID
ALTER TABLE person ENABLE ROW LEVEL SECURITY;
CREATE POLICY person_user_policy ON person USING (user_id = current_setting('app.logged_user_id')::INT);

ALTER TABLE relation ENABLE ROW LEVEL SECURITY;
CREATE POLICY relation_user_policy ON relation USING (user_id = current_setting('app.logged_user_id')::INT);

ALTER TABLE photo ENABLE ROW LEVEL SECURITY;
CREATE POLICY photo_user_policy ON photo USING (user_id = current_setting('app.logged_user_id')::INT);

ALTER TABLE photo_person ENABLE ROW LEVEL SECURITY;
CREATE POLICY photo_person_user_policy ON photo_person USING (user_id = current_setting('app.logged_user_id')::INT);
