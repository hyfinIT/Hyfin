package hyphin.dialect;

import org.hibernate.MappingException;

public class EmptyDialect extends org.hibernate.dialect.Dialect {
    @Override
    public String getSequenceNextValString(String sequenceName) throws MappingException {
        return "select HYFIN.PUBLIC." + sequenceName + ".nextVal";
    }
}


