package hyphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ELEMENTS", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class Elements {
    @Id
    String elementID;
    String elementType;
    String elementPosition;
    String mediaLocation;
    String clickThroughPosition;
    String videoPosition;
    String inModuleGamePosition;
    String endChallengePosition;
}
