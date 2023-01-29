package hyphin.dialect;

import org.hibernate.MappingException;

import java.sql.Types;

import static org.hibernate.type.StandardBasicTypes.CALENDAR_DATE;

public class EmptyDialect extends org.hibernate.dialect.Dialect {

    public EmptyDialect() {
        super();
        registerColumnType( Types.TIMESTAMP, "datetime" );
        registerHibernateType(444, CALENDAR_DATE.getName());
    }

    @Override
    public String getSequenceNextValString(String sequenceName) throws MappingException {
        return "select HYFIN.PUBLIC." + sequenceName + ".nextVal";
    }
}


