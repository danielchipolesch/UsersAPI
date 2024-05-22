package br.com.cotiinformatica.application.handlers;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import br.com.cotiinformatica.domain.exceptions.EmailAlreadyRegisteredException;

@ControllerAdvice
public class UnprocessableEntityHandler {
	
	@ExceptionHandler(EmailAlreadyRegisteredException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public List<String> errorHandler(EmailAlreadyRegisteredException e) {
		
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		return errors;
	}	
}