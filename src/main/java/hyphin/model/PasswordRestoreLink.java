package hyphin.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordRestoreLink {
    String hash;
    Long timestamp;
    User user;
}
