package br.com.rperatello.book_api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.rperatello.book_api.data.vo.v1.BookResponseVO;
import br.com.rperatello.book_api.model.interfaces.IBookService;
import br.com.rperatello.book_api.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Books", description = "Endpoints for books information")
public class BookController {
	
	@Autowired
	private IBookService bookService;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON })	
	@Operation(
		summary = "Finds all books", description = "Finds all books in database",
		tags = {"Books"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = {
					@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = PagedModel.class)
					)
			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)	
	public ResponseEntity<PagedModel<EntityModel<BookResponseVO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "sort", defaultValue = "asc") String sort
	){
		
		var sortDirection = "desc".equalsIgnoreCase(sort)? Direction.DESC : Direction.ASC;			
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
		return ResponseEntity.ok(bookService.findAll(pageable));
		
	}
	
	
	@GetMapping(
			value = "/{id}",					
			produces = { MediaType.APPLICATION_JSON }
	)
	@Operation(
		summary = "Finds book by ID", description = "Finds a books in database by ID",
		tags = {"Books"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = {
					@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = BookResponseVO.class)
					)
			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<BookResponseVO> findById(
				@PathVariable(value = "id") Long id,
				HttpServletRequest request,
				HttpServletResponse response
				) {
		var res = bookService.findById(id);
		
		String lastViewed = request.getCookies() == null ? "" : Arrays.stream(request.getCookies())
                .filter(cookie -> "lastViewed".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
		
		List<String> ids = new ArrayList<String>();
		
		if(!StringUtils.isEmpty(lastViewed)) {
			ids = Arrays.stream(lastViewed.split("\\|"))
			.map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
		}

		if(!ids.isEmpty()) {
	        ids.remove(id.toString());
        }
		
		ids.add(0, id.toString());

        if (ids.size() > 5)
            ids = ids.subList(0, 5);        

        Cookie cookie = new Cookie("lastViewed", String.join("|", ids));
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(cookie);
        
		return ResponseEntity.ok(res);
		
	}
	
	@GetMapping(
			value = "/genre/{genre}",
			produces = { MediaType.APPLICATION_JSON }
	)
	@Operation(
		summary = "Finds book by genre", description = "Finds a books in database by genre",
		tags = {"Books"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = {
					@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = PagedModel.class)
					)
			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<PagedModel<EntityModel<BookResponseVO>>> findByGenre(
			@PathVariable(value = "genre") String genre,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "sort", defaultValue = "asc") String sort
	){
		
		var sortDirection = "desc".equalsIgnoreCase(sort)? Direction.DESC : Direction.ASC;			
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
		var res = bookService.findByGenre(pageable, genre);
		return ResponseEntity.ok(res);
		
	}
	
	@GetMapping(
			value = "/author/{author}",
			produces = { MediaType.APPLICATION_JSON }
	)
	@Operation(
		summary = "Finds book by author", description = "Finds a books in database by author",
		tags = {"Books"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = {
					@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = PagedModel.class)
					)
			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<PagedModel<EntityModel<BookResponseVO>>> findByAuthor(
			@PathVariable(value = "author") String author,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "sort", defaultValue = "asc") String sort			
	){
		
		var sortDirection = "desc".equalsIgnoreCase(sort)? Direction.DESC : Direction.ASC;			
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
		var res = bookService.findByAuthor(pageable, author);
		return ResponseEntity.ok(res);
		
	}	
	
	@GetMapping(
		value = "/findLastViewedRecords",
		produces = { MediaType.APPLICATION_JSON }
	)
	@Operation(
		summary = "Finds last book viewed", description = "Finds last book viewed in database",
		tags = {"Books"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = {
					@Content(
						mediaType = "application/json",
						array = @ArraySchema(schema = @Schema(implementation = BookResponseVO.class))
					)
			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<List<BookResponseVO>> findLastViewedRecords(
			@CookieValue(value = "lastViewed", defaultValue = "") String lastViewed
			){
		
		var res = bookService.findLastViewedRecords(lastViewed);
		return ResponseEntity.ok(res);
		
	}	

}
