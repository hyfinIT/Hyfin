package hyphin.dialect;

import javax.persistence.AttributeConverter;
import java.time.LocalDate;

public class DateConverter implements AttributeConverter<LocalDate, String> {
    @Override
    public String convertToDatabaseColumn(LocalDate localDate) {
        return localDate.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return LocalDate.parse(s);
    }
}
