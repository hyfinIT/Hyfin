package hyphin.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ELEMENTS", schema="PUBLIC", catalog = "HYFIN")
@Getter
@Setter
public class Element {
    @Id
    String elementID;
    String elementType;
    String mediaLocation;
    String clickThroughPosition;
    String videoPosition;
    String inModuleGamePosition;
    String endChallengePosition;
}
