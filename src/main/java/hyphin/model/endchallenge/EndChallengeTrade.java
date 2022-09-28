package hyphin.model.endchallenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndChallengeTrade {

    private String direction;
    private int capitalPercent;
    private int price;
}
