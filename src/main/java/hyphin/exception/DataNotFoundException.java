package hyphin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Or @ResponseStatus(HttpStatus.NO_CONTENT)
public class DataNotFoundException extends RuntimeException {

}
