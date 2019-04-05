package gestao.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LeitoNotFoundExcpetion extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String MESSAGE = "Leito nao encontrado";

	public LeitoNotFoundExcpetion() {
		super(MESSAGE);
	}

	public LeitoNotFoundExcpetion(ObjectId id) {
		super(MESSAGE + ": " + id);
	}

	public LeitoNotFoundExcpetion(String id) {
		super(MESSAGE + ": " + id);
	}

}
