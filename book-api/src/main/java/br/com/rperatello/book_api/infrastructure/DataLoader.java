package br.com.rperatello.book_api.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.rperatello.book_api.model.interfaces.IBookService;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private IBookService bookService;

    @Override
    public void run(String... args) throws Exception {
        String filePath = "src\\main\\resources\\fileCSV\\Books_df.csv";
        bookService.uploadBooksByCSV(filePath);
    }
}