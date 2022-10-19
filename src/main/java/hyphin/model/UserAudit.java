package hyphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "UIDAUDIT", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor

public class UserAudit {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "userAuditSequence")
    int id;
    int uid;
    String activityType;
    String dateTime;
    String glossaryTerm;
    String mediaType;
    String moduleProgressPosition;
    String module;
    String learningJourney;
    String learningJourneyId;
    String moduleId;
    String elementId;
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





