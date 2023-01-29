package hyphin.model.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "OPSAUDIT", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class OperationAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_op_aud")
    @SequenceGenerator(name = "id_op_aud", sequenceName = "OPSAUDITSEQUENCE")
    private Long id;

    private String name;

    private String dateTime;

    private String status;
}
