package hyphin.model.click;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CLICK_ITEM", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class ClickItem {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "clickItemSequence")
    private Long id;
    private Integer number;
    private String text;
    @Column(name = "IMAGE_DATA")
    private String imageData;
    private String style;
    @Column(name = "COURSE_ID")
    private String courseId;
    @Column(name = "MODULE_ID")
    private String moduleId;
    @Column(name = "ELEMENTID")
    private String elementId;
    @Column(name = "VERSION_REFERENCE")
    private String versionReference;
}
