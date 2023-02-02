package hyphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "UIDAUDIT", schema = "PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class UserAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "userAuditSequence")
    @ToString.Include
    int id;
    @ToString.Include
    int uid;
    @ToString.Include
    String activityType;
    @ToString.Include
    String dateTime;
    String glossaryTerm;
    String mediaType;
    String moduleProgressPosition;
    String module;
    String learningJourney;
    String learningJourneyId;
    String moduleId;
    @ToString.Include
    String elementId;
    @ToString.Include
    String elementStatus;
    String completionTime;
    String elementPosition;
    String quidNumber;
    String quidNumberOutcome;
    String quidAnswer;
    String difficulty;
    String screenIdNumber;
    String mediaDuration;
    String mediaCompletionStatus;
    String text1;
}





