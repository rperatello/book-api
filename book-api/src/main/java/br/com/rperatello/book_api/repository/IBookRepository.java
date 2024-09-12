package br.com.rperatello.book_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.rperatello.book_api.model.Book;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {	

	@Query("SELECT b FROM Book b WHERE TRIM(LOWER(b.author)) = TRIM(LOWER(:author))")
    Page<Book> findByAuthor(@Param("author") String author, Pageable pageable);

	
	@Query("SELECT b FROM Book b WHERE TRIM(LOWER(b.mainGenre)) = TRIM(LOWER(:genre))")
    Page<Book> findByMainGenre(@Param("genre") String genre, Pageable pageable);
	
}