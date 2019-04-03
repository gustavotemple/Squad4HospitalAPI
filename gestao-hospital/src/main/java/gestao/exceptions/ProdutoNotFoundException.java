package gestao.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProdutoNotFoundException extends RuntimeException {

	private static String MESSAGE = "Produto nao encontrado";

	public ProdutoNotFoundException() {
		super(MESSAGE);
	}

	public ProdutoNotFoundException(ObjectId id) {
		super(MESSAGE + ": " + id);
	}
	
	public ProdutoNotFoundException(String id) {
		super(MESSAGE + ": " + id);
	}
}
