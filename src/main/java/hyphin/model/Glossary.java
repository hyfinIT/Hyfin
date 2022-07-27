package hyphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "GLOSSARY", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class Glossary {
    @Id
    int id;
    String moduleId;
    String learningJourney;
    String definition;
    String glossaryTerm;
    String productType;

}
