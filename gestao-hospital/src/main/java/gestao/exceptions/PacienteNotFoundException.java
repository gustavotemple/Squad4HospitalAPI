package gestao.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PacienteNotFoundException extends RuntimeException {

	private static String MESSAGE = "Paciente nao encontrado";

	public PacienteNotFoundException() {
		super(MESSAGE);
	}

	public PacienteNotFoundException(ObjectId id) {
		super(MESSAGE + ": " + id);
	}
	
	public PacienteNotFoundException(String id) {
		super(MESSAGE + ": " + id);
	}
}
