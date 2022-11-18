package hyphin.dto;

import hyphin.enums.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndChallengeTradeDto {

    private Sentiment sentiment;
    private int capitalPercent;
    private int price;
}
