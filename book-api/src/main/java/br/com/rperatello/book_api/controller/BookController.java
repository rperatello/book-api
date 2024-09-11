package br.com.rperatello.book_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.rperatello.book_api.model.Book;
import br.com.rperatello.book_api.model.interfaces.IBookService;
import br.com.rperatello.book_api.util.MediaType;

@RestController
@RequestMapping("/api/book/v1")
public class BookController {
	
	@Autowired
	private IBookService service;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })		
//	public ResponseEntity<PagedModel<EntityModel<Book>>> findAll(
			public ResponseEntity<Page<Book>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "sort", defaultValue = "asc") String sort
			) {
		
		var sortDirection = "desc".equalsIgnoreCase(sort)
				? Direction.DESC : Direction.ASC;
			
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
		return ResponseEntity.ok(service.findAll(pageable));
	}

}
