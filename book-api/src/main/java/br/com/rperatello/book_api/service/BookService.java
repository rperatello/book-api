package br.com.rperatello.book_api.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import br.com.rperatello.book_api.util.serialization.converter.GsonUtil;
import jakarta.transaction.Transactional;
import net.spy.memcached.MemcachedClient;

@Service
public class BookService implements IBookService {
	
	private Logger logger = Logger.getLogger(BookService.class.getName());
	
	@Autowired
    private IBookRepository bookRepository;
	
	@Autowired
	private MemcachedClient memcachedClient;;
	
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
		
		var lastId = memcachedClient.get("lastId");
		if (lastId != null && lastId.equals(id.toString())) {
			var lastObjSaved = memcachedClient.get("lastObj");
			if (lastObjSaved != null) {	
				logger.info("Returned response by cache ...");
				BookResponseVO parsedObj = (BookResponseVO)GsonUtil.Desserialize(lastObjSaved.toString(), BookResponseVO.class );
				return addBookHateoasLinks(parsedObj);	
			}			
		}		
		var book = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		var vo = Mapper.parseObject(book, BookResponseVO.class);
		
		logger.info("Saving response in cache ...");
		memcachedClient.set("lastId", 60, id.toString());
		memcachedClient.set("lastObj", 60, GsonUtil.Serialize(vo));
		
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
	
	public List<BookResponseVO> findLastViewedRecords(String lastViewed){		

		logger.info(String.format("Finding last viewed records - ID´s: %s ...", lastViewed));
		
		List<Integer> ids = new ArrayList<Integer>();
		
		if (!StringUtils.isBlank(lastViewed) && lastViewed.matches("^[0-9|]+$"))
			ids = Arrays.stream(lastViewed.split("\\|"))
					.map(String::trim)
	                .filter(s -> !s.isEmpty())
	                .map(Integer::parseInt)
	                .collect(Collectors.toList());			
		
		List<BookResponseVO> books = new ArrayList<BookResponseVO>();
		
		if (!ids.isEmpty()) 
			books = Mapper.parseListObjects(bookRepository.findLastViewedRecords(ids), BookResponseVO.class);
		
		books.stream()
			.forEach(p -> addBookHateoasLinks(p));	
		
		return books;

	}
	
	public BookResponseVO addBookHateoasLinks( BookResponseVO objSaved) {
		try {
			if (objSaved == null) throw new ResourceNotFoundException("Book data is required");
			return objSaved.add(linkTo(methodOn(BookController.class).findById(objSaved.getKey(), null, null)).withSelfRel());
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, String.format("addBookHateoasLinks - Error: %s ", e.getMessage()));
			return objSaved;
		}
	}	

}
