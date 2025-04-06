package db6.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Relation {
    // TODO: use plain enum
    public enum Type {
        CHILD("CHILD"), SPOUSE("SPOUSE");

        String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person1", nullable = false)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Person person1;

    @ManyToOne
    @JoinColumn(name = "person2", nullable = false)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Person person2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    public Relation() {
    }

    public Relation(Person person1, Person person2, Type type) {
        this.person1 = person1;
        this.person2 = person2;
        this.type = type;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson1() {
        return person1;
    }

    public void setPerson1(Person person1) {
        this.person1 = person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
