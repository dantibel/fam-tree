package db6.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RelationTypeConverter implements AttributeConverter<Relation.Type, String> {
    @Override
    public String convertToDatabaseColumn(Relation.Type relationType) {
        return relationType.toString();
    }

    @Override
    public Relation.Type convertToEntityAttribute(String relationType) {
        return Relation.Type.valueOf(relationType.toUpperCase());
    }
}
