package br.com.rperatello.book_api.model.interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import br.com.rperatello.book_api.data.vo.v1.BookResponseVO;

public interface IBookService {
	
	void uploadBooksByCSV(String filePath); 
	
	PagedModel<EntityModel<BookResponseVO>> findAll(Pageable pageable);
	
	BookResponseVO findById(Long id);
	
	PagedModel<EntityModel<BookResponseVO>> findByGenre(Pageable pageable, String genre);
	
	PagedModel<EntityModel<BookResponseVO>> findByAuthor(Pageable pageable, String author);
	
	List<BookResponseVO> findLastViewedRecords(String lastViewed);

}
