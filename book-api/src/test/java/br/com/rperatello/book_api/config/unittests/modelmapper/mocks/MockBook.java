package br.com.rperatello.book_api.config.unittests.modelmapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.rperatello.book_api.data.vo.v1.BookResponseVO;
import br.com.rperatello.book_api.model.Book;

public class MockBook {

	public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookResponseVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> persons = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<BookResponseVO> mockVOList() {
        List<BookResponseVO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }
    
    public Book mockEntity(Integer number) {
    	Book book = new Book();
    	book.setId(number.longValue());
    	book.setTitle("Title " + number);
    	book.setMainGenre("MainGenre " + number);
    	book.setSubGenre("SubGenre " + number);
    	book.setType("Type " + number);
    	book.setCurrency("INS");
    	book.setPrice((double)number);
    	book.setRating((double)number);
    	book.setPeopleRated(number);
    	book.setUrl("http//:www.rperatello.com/books/" + number);
        return book;
    }

    public BookResponseVO mockVO(Integer number) {
    	BookResponseVO book = new BookResponseVO();
    	book.setKey(number.longValue());
    	book.setTitle("Title " + number);
    	book.setMainGenre("MainGenre " + number);
    	book.setSubGenre("SubGenre " + number);
    	book.setType("Type " + number);
    	book.setCurrency("INS");
    	book.setPrice((double)number);
    	book.setRating((double)number);
    	book.setPeopleRated(number);
    	book.setUrl("http//:www.rperatello.com/books/" + number);
        return book;
    }

}
