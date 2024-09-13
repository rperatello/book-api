package br.com.rperatello.book_api.config.unittests.modelmapper;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.rperatello.book_api.config.unittests.modelmapper.mocks.MockBook;
import br.com.rperatello.book_api.data.vo.v1.BookResponseVO;
import br.com.rperatello.book_api.mapper.Mapper;
import br.com.rperatello.book_api.model.Book;

public class ModelMapperTest {

	MockBook inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

	@Test
    public void parseEntityToVOTest() {
    	BookResponseVO output = Mapper.parseObject(inputObject.mockEntity(), BookResponseVO.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("Title 0", output.getTitle());
        assertEquals("MainGenre 0", output.getMainGenre());
        assertEquals("SubGenre 0", output.getSubGenre());
        assertEquals("Type 0", output.getType());
        assertEquals("INS", output.getCurrency());
        assertEquals(Double.valueOf(0), output.getRating());
        assertEquals(0, output.getPeopleRated());
        assertEquals("http//:www.rperatello.com/books/0", output.getUrl());      

    }

    @Test
    public void parseEntityListToVOListTest() {
        List<BookResponseVO> outputList = Mapper.parseListObjects(inputObject.mockEntityList(), BookResponseVO.class);
        
        BookResponseVO output0 = outputList.get(0);    
        
        assertEquals(Long.valueOf(0L), output0.getKey());
        assertEquals("Title 0", output0.getTitle());
        assertEquals("MainGenre 0", output0.getMainGenre());
        assertEquals("SubGenre 0", output0.getSubGenre());
        assertEquals("Type 0", output0.getType());
        assertEquals("INS", output0.getCurrency());
        assertEquals(Double.valueOf(0), output0.getRating());
        assertEquals(0, output0.getPeopleRated());
        assertEquals("http//:www.rperatello.com/books/0", output0.getUrl()); 
        
        BookResponseVO output5 = outputList.get(5);
        
        assertEquals(Long.valueOf(5L), output5.getKey());
        assertEquals("Title 5", output5.getTitle());
        assertEquals("MainGenre 5", output5.getMainGenre());
        assertEquals("SubGenre 5", output5.getSubGenre());
        assertEquals("Type 5", output5.getType());
        assertEquals("INS", output5.getCurrency());
        assertEquals(Double.valueOf(5), output5.getRating());
        assertEquals(5, output5.getPeopleRated());
        assertEquals("http//:www.rperatello.com/books/5", output5.getUrl()); 
    }

    @Test
    public void parseVOToEntityTest() {
    	Book output = Mapper.parseObject(inputObject.mockVO(), Book.class);
    	assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Title 0", output.getTitle());
        assertEquals("MainGenre 0", output.getMainGenre());
        assertEquals("SubGenre 0", output.getSubGenre());
        assertEquals("Type 0", output.getType());
        assertEquals("INS", output.getCurrency());
        assertEquals(Double.valueOf(0), output.getRating());
        assertEquals(0, output.getPeopleRated());
        assertEquals("http//:www.rperatello.com/books/0", output.getUrl());   
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Book> outputList = Mapper.parseListObjects(inputObject.mockVOList(), Book.class);
        
        Book output0 = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), output0.getId());
        assertEquals("Title 0", output0.getTitle());
        assertEquals("MainGenre 0", output0.getMainGenre());
        assertEquals("SubGenre 0", output0.getSubGenre());
        assertEquals("Type 0", output0.getType());
        assertEquals("INS", output0.getCurrency());
        assertEquals(Double.valueOf(0), output0.getRating());
        assertEquals(0, output0.getPeopleRated());
        assertEquals("http//:www.rperatello.com/books/0", output0.getUrl()); 
        
        Book output5 = outputList.get(5);
        
        assertEquals(Long.valueOf(5L), output5.getId());
        assertEquals("Title 5", output5.getTitle());
        assertEquals("MainGenre 5", output5.getMainGenre());
        assertEquals("SubGenre 5", output5.getSubGenre());
        assertEquals("Type 5", output5.getType());
        assertEquals("INS", output5.getCurrency());
        assertEquals(Double.valueOf(5), output5.getRating());
        assertEquals(5, output5.getPeopleRated());
        assertEquals("http//:www.rperatello.com/books/5", output5.getUrl()); 
    }
}