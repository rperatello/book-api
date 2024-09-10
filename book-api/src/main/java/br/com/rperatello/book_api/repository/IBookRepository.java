package br.com.rperatello.book_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rperatello.book_api.model.Book;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {
	
	Book findByAuthor(String author);
	
	Book findByMainGenre(String mainGenre);
	
}