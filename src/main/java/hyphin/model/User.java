package hyphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "UIDTABLE", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    int uid;
    String clientType;
    String firstName;
    String surName;
    String email;
    String password;
    String dateTime;
    private Boolean active;
    @Column(name = "DELETION_DATE")
    private Long deletionDate;
    private String region;
    @Column(name = "PREFERENCE_TYPE")
    private String preferenceType;
    @Column(name = "DISPLAY_PRIORITY")
    private String displayPriority;
}
