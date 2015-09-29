package net.cworks.wowreg.mongo;

import org.joda.time.DateTime;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.MappingException;

import java.util.Date;

public class JodaDateTimeConverter extends TypeConverter implements SimpleValueConverter {

    public JodaDateTimeConverter() {
        super(DateTime.class);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        return value == null ? null : new Date(((DateTime) value).getMillis());
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object decode(Class c, Object o, MappedField i) throws MappingException {
        if (o == null) {
            return null;
        } else if (o instanceof DateTime) {
            return o;
        } else if (o instanceof Number) {
            return new DateTime(((Number) o).longValue());
        } else if (o instanceof Date) {
            return new DateTime(((Date) o).getTime());
        } else {
            return new DateTime(o);
        }
    }
}
