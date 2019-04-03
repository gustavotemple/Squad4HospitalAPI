package gestao.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class HospitalNotFoundException extends RuntimeException {

	private static String MESSAGE = "Hospital nao encontrado";

	public HospitalNotFoundException() {
		super(MESSAGE);
	}

	public HospitalNotFoundException(ObjectId id) {
		super(MESSAGE + ": " + id);
	}
	
	public HospitalNotFoundException(String id) {
		super(MESSAGE + ": " + id);
	}
}
