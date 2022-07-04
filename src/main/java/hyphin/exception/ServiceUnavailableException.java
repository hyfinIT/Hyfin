package hyphin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Or @ResponseStatus(HttpStatus.NO_CONTENT)
public class ServiceUnavailableException extends RuntimeException {

}
