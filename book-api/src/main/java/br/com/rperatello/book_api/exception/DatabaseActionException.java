package br.com.rperatello.book_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DatabaseActionException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public DatabaseActionException() {
		super("Database action refused");
	}
	
	public DatabaseActionException(String ex) {
		super(ex);
	}
}