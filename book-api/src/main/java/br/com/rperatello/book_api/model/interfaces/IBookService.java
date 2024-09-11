package br.com.rperatello.book_api.model.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.rperatello.book_api.model.Book;

public interface IBookService {
	
	void uploadBooksByCSV(String filePath);
	
	Page<Book> findAll(Pageable pageable);

}
