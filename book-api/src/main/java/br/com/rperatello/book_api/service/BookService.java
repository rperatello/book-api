package br.com.rperatello.book_api.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import br.com.rperatello.book_api.model.Book;
import br.com.rperatello.book_api.model.interfaces.IBookService;
import br.com.rperatello.book_api.repository.IBookRepository;
import jakarta.transaction.Transactional;

@Service
public class BookService implements IBookService {
	
	private Logger logger = Logger.getLogger(BookService.class.getName());
	
	@Autowired
    private IBookRepository bookRepository;
	
//	@Autowired
//	PagedResourcesAssembler<Book> assembler;

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
    
	public Page<Book> findAll(Pageable pageable){

		logger.info("Finding all books!");

		var booksPage = bookRepository.findAll(pageable);

//		var booksVOs = booksPage.map(p -> DozerMapper.parseObject(p, BookVO.class));
//		booksVOs.map(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
//		
//		Link findAllLink = linkTo(
//		          methodOn(BookController.class)
//		          	.findAll(pageable.getPageNumber(),
//	                         pageable.getPageSize(),
//	                         "asc")).withSelfRel();
//		
//		return assembler.toModel(booksVOs, findAllLink);
		
		return booksPage;
	}

}
