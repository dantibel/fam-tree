package db6.domain;

import java.util.Optional;

// Encapsulation of persons' father and mother
public class Parents {
    private Optional<Person> father;
    private Optional<Person> mother;

    public Parents() {
        this.father = Optional.empty();
        this.mother = Optional.empty();
    }

    public Parents(Person father, Person mother) {
        this.father = Optional.ofNullable(father);
        this.mother = Optional.ofNullable(mother);
    }

    public Optional<Person> getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = Optional.ofNullable(father);
    }

    public Optional<Person> getMother() {
        return mother;
    }

    public void setMother(Person mother) {
        this.mother = Optional.ofNullable(mother);
    }

    public boolean hasAny() {
        return father.isPresent() || mother.isPresent();
    }
}
