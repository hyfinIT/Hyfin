package hyphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "GAMEQUESTIONS", schema = "PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class GameQuestions {

    @Id
    String quidNumber;
    String elementId;
    String elementName;
    String questionText;
    String questionPolyMorph;
    String answerOption01;
    String answerOption02;
    String answerCorrect;
    String difficulty;
    String quvers;
    String namingReference;


}
