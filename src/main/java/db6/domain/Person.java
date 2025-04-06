package db6.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Person {
    // TODO: use plain enum
    public enum Gender {
        MALE("MALE"), FEMALE("FEMALE"), OTHER("OTHER");

        String value;

        Gender(String value) {
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

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(length = 50)
    private String middleName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "portrait_url", length = 255)
    private String portraitUrl;

    private LocalDate birthDate;

    private LocalDate deathDate;

    public Person() {
    }

    public Person(String firstName, String middleName, String lastName, Gender gender, String portraitUrl, LocalDate birthDate, LocalDate deathDate) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.portraitUrl = portraitUrl;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }
}
