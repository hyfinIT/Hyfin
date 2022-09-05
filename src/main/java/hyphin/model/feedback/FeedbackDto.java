package hyphin.model.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class FeedbackDto {

    @NotBlank
    private String name;
    @NotBlank
    private String email;
    private String organization;
    private String phone;
    private String message;
}
