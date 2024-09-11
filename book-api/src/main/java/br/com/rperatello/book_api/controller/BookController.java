package br.com.rperatello.book_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.rperatello.book_api.data.vo.v1.BookResponseVO;
import br.com.rperatello.book_api.model.interfaces.IBookService;
import br.com.rperatello.book_api.util.MediaType;

@RestController
@RequestMapping("/api/book/v1")
public class BookController {
	
	@Autowired
	private IBookService bookService;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })	
	public ResponseEntity<PagedModel<EntityModel<BookResponseVO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "sort", defaultValue = "asc") String sort
			) {
		
		var sortDirection = "desc".equalsIgnoreCase(sort)
				? Direction.DESC : Direction.ASC;
			
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
		return ResponseEntity.ok(bookService.findAll(pageable));
	}
	
	@GetMapping(
			value = "/{id}",
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML }
	)	
	public BookResponseVO findById(@PathVariable(value = "id") Long id) {
		var res = bookService.findById(id);
		return res;
	}

}
