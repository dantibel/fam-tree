package db6.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<Person.Gender, String> {
    @Override
    public String convertToDatabaseColumn(Person.Gender gender) {
        return gender.toString();
    }

    @Override
    public Person.Gender convertToEntityAttribute(String gender) {
        return Person.Gender.valueOf(gender.toUpperCase());
    }
}
