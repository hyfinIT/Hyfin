package hyphin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FLAG_ICONSDB", schema = "PUBLIC", catalog = "HYFIN")
@Getter
@Setter
public class FlagIcon {

    @Id
    private Long id;
    @Column(name = "CCY_MNEMONIC")
    private String ccyMnemonic;
    private String country;
    @Column(name = "FLAG_FILE")
    private String flagFile;
}
