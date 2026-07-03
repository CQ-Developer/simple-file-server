package com.chen.sfs.repository.jpa.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.nio.file.Path;

@Converter(autoApply = true)
public class PathAttributeConverter implements AttributeConverter<Path, String> {

        @Override
        public String convertToDatabaseColumn(Path attribute) {
                if (attribute == null) {
                        return null;
                }
                return attribute.normalize().toAbsolutePath().toString();
        }

        @Override
        public Path convertToEntityAttribute(String dbData) {
                if (dbData == null) {
                        return null;
                }
                return Path.of(dbData);
        }

}
