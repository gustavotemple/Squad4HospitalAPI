package gestao.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadEnderecoException extends RuntimeException {

	private static String MESSAGE = "Endereco invalido";

	public BadEnderecoException() {
		super(MESSAGE);
	}
	
}
