package br.com.rperatello.book_api.model.interfaces;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import br.com.rperatello.book_api.model.Book;

public interface IBookService {
	
	void uploadBooksByCSV(String filePath); 
	
	PagedModel<EntityModel<Book>> findAll(Pageable pageable);

}
