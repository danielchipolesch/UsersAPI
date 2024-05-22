package br.com.cotiinformatica.domain.exceptions;

public class AccessDeniedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AccessDeniedException() {
		super("Acesso negado. Usuário inválido.");
	}

}
