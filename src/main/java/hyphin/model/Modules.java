package hyphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "MODULES", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class Modules {
    @Id
    String moduleId;
    String moduleName;
    String elementName;
    String elementPosition;
    String elementType;
    String elementId;

}
