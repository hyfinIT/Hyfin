package hyphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "LEARNINGJOURNEYS", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class LearningJourneys {
    @Id
    int id;
    String classification;
    String learningJourneyId;
    String learningJourneyName;
    String moduleId;
    String modulePosition;
    String moduleName;

}
