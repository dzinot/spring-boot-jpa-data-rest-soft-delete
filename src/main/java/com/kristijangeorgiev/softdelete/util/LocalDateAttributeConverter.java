package com.kristijangeorgiev.softdelete.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 
 * @author Kristijan Georgiev
 * 
 *         Custom converter class that enables usage of LocalDate with the DB
 *
 */

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
		return (localDateTime == null ? null : Timestamp.valueOf(localDateTime));
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
		return (timestamp == null ? null : timestamp.toLocalDateTime());
	}
}
