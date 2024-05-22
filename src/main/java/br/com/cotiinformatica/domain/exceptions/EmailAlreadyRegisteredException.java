package br.com.cotiinformatica.domain.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailAlreadyRegisteredException (String email) {
		super("O e-mail "+ email +" já está cadastrado, tente outro.");
	}
	
	
}
