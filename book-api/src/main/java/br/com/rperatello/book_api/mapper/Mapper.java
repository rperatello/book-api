package br.com.rperatello.book_api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;

import br.com.rperatello.book_api.data.vo.v1.BookResponseVO;
import br.com.rperatello.book_api.model.Book;

public class Mapper {
	
	private static Logger logger = Logger.getLogger(Mapper.class.getName());

	private static ModelMapper mapper = new ModelMapper();
	
	static {
		mapper.getConfiguration().setAmbiguityIgnored(true);
		
		mapper.createTypeMap(Book.class, BookResponseVO.class)
		.addMapping(Book::getId, BookResponseVO::setKey);
		
	}
	
	public static <O, D> D parseObject(O origin, Class<D> destination) {
		return mapper.map(origin, destination);
	}
	
	public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
		List<D> destinationObjects = new ArrayList<D>();
		for (O o : origin) {
			destinationObjects.add(mapper.map(o, destination));
		}
		return destinationObjects;
	}
	
	public static <O, D> D copyProperties (O origin, Class<D> destination) {
		return mapper.map(origin, destination);
	}	

}