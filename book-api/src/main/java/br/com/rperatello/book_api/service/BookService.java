package br.com.rperatello.book_api.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import br.com.rperatello.book_api.controller.BookController;
import br.com.rperatello.book_api.data.vo.v1.BookResponseVO;
import br.com.rperatello.book_api.exception.RequiredObjectIsNullException;
import br.com.rperatello.book_api.exception.ResourceNotFoundException;
import br.com.rperatello.book_api.mapper.Mapper;
import br.com.rperatello.book_api.model.Book;
import br.com.rperatello.book_api.model.interfaces.IBookService;
import br.com.rperatello.book_api.repository.IBookRepository;
import jakarta.transaction.Transactional;

@Service
public class BookService implements IBookService {
	
	private Logger logger = Logger.getLogger(BookService.class.getName());
	
	@Autowired
    private IBookRepository bookRepository;
	
	@Autowired
	PagedResourcesAssembler<BookResponseVO> assembler;

    @Transactional
	@Override
	public void uploadBooksByCSV(String filePath) {
		
		if (!bookRepository.findAll().isEmpty()) {
			logger.info("BookService - uploadBooksByCSV | Table has data. CSV not loaded");
            return;
        }

		logger.info("BookService - uploadBooksByCSV | Loading CSV");
        List<Book> books = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            
            List<String[]> rows = reader.readAll();
            
            rows.stream().skip(1).forEach(row -> {
            	String title = row[1];
                String author = row[2];
                String mainGenre = row[3];
                String subGenre = row[4];
                String type = row[5];
                
                String currency = "INR";
                String symbolWithPrice = row[6];
                double price = symbolWithPrice != null && symbolWithPrice.trim().length() > 1 ? Double.parseDouble(symbolWithPrice.replace(",", "").substring(1)) : 0d;
                
                double rating = Double.parseDouble(row[7]);
                int peopleRated = (int)Double.parseDouble(row[8]);
                String url = row[9];                

                Book book = new Book();
                book.setTitle(title);
                book.setAuthor(author);
                book.setMainGenre(mainGenre);
                book.setSubGenre(subGenre);
                book.setType(type);
                book.setCurrency(currency);
                book.setPrice(price);
                book.setRating(rating);
                book.setPeopleRated(peopleRated);
                book.setUrl(url);
                books.add(book);
            });
            
        } catch (IOException e) {
        	logger.log(Level.SEVERE, String.format("BookService - uploadBooksByCSV | Error: %s ", e.getMessage()), e);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, String.format("BookService - uploadBooksByCSV | Error: %s ", e.getMessage()), e);
        }

        bookRepository.saveAll(books);
        logger.info("BookService - uploadBooksByCSV | CSV Loaded.");
		
	}
    
	public PagedModel<EntityModel<BookResponseVO>> findAll(Pageable pageable){

		logger.info("Finding all books ...");

		var booksPage = bookRepository.findAll(pageable);
		
		var booksVO = booksPage.map(b -> Mapper.parseObject(b, BookResponseVO.class));
		booksVO.map(p -> addBookHateoasLinks(p));

		Link findAllLink = linkTo(
		          methodOn(BookController.class)
		          	.findAll(
		          			pageable.getPageNumber(),
	                        pageable.getPageSize(),
	                        "asc"
                    )).withSelfRel();
		
		return assembler.toModel(booksVO, findAllLink);
	}
	
	@Override
	public BookResponseVO findById(Long id) {
		logger.info(String.format("Find book with ID %s ...", id));
		var book = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		var vo = Mapper.parseObject(book, BookResponseVO.class);
		return addBookHateoasLinks(vo);
	}
	
	public PagedModel<EntityModel<BookResponseVO>> findByGenre(Pageable pageable, String genre){
	
		logger.info(String.format("Finding by genre %s ...", genre));
		
		if (StringUtils.isBlank(genre)) throw new RequiredObjectIsNullException("Genre is null or empty");

		var books = bookRepository.findByMainGenre(genre, pageable);
		var booksVO = books.map(b -> Mapper.parseObject(b, BookResponseVO.class));
		booksVO.map(p -> addBookHateoasLinks(p));		

		Link findAllLink = linkTo(
		          methodOn(BookController.class)
		          	.findByGenre(
		          			genre,
		          			pageable.getPageNumber(),
	                        pageable.getPageSize(),
	                        "asc"
                    )).withSelfRel();
		
		return assembler.toModel(booksVO, findAllLink);

	}
	
	public PagedModel<EntityModel<BookResponseVO>> findByAuthor(Pageable pageable, String author){		

		logger.info(String.format("Finding by author %s ...", author));
		
		if (StringUtils.isBlank(author)) throw new RequiredObjectIsNullException("Author is null or empty");

		var books = bookRepository.findByAuthor(author, pageable);
		var booksVO = books.map(b -> Mapper.parseObject(b, BookResponseVO.class));
		booksVO.map(p -> addBookHateoasLinks(p));		

		Link findAllLink = linkTo(
		          methodOn(BookController.class)
		          	.findByAuthor(
		          			author,
		          			pageable.getPageNumber(),
	                        pageable.getPageSize(),
	                        "asc"
                    )).withSelfRel();
		
		return assembler.toModel(booksVO, findAllLink);

	}
	
	public BookResponseVO addBookHateoasLinks( BookResponseVO vo) {
		try {
			if (vo == null) throw new ResourceNotFoundException("Book data is required");
			return vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, String.format("addBookHateoasLinks - Error: %s ", e.getMessage()));
			return vo;
		}
	}	

}
