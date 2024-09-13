package br.com.rperatello.book_api.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import br.com.rperatello.book_api.model.interfaces.IBookService;

@Component
public class DataLoader implements CommandLineRunner {
	
	private Logger logger = Logger.getLogger(DataLoader.class.getName());

    @Autowired
    private IBookService bookService;   

    @Value("${file.csv.path:#{null}}")
    private String filePath;
    
    @Autowired
    private ResourceLoader resourceLoader;

    private final List<String> expectedColumns = Arrays.asList("","Title","Author","Main Genre","Sub Genre","Type","Price","Rating","No. of People rated","URLs");
    private static final String EXPECTED_FILE_NAME = "Books_df.csv";

    
    @Override
    public void run(String... args) throws Exception {
        if (isValidFilePath(filePath)) {

            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
            	logger.log(Level.SEVERE, "DataLoader - File does not exist or is not a valid file. Skipping CSV load.");
                return;
            }

            if (!validateCSV(filePath)) {
            	logger.log(Level.SEVERE, "DataLoader - CSV file does not have the required columns. Skipping CSV load.");
                return;
            }
            
            logger.log(Level.INFO, String.format("DataLoader - Get CSV in folder: %s", filePath));

            bookService.uploadBooksByCSV(filePath);
            
        } else {
        	
        	String filePath = "src\\main\\resources\\fileCSV\\Books_df.csv";
        	
        	Resource resource = resourceLoader.getResource("classpath:fileCSV/Books_df.csv");
        	
        	if (resource.exists())
        		filePath = resource.getFile().getPath();
        	

        	if (!validateCSV(filePath)) {
        		logger.log(Level.SEVERE, "DataLoader - CSV file in classpath does not have the required columns. Skipping CSV load.");
					return;
        	}

        	logger.log(Level.INFO, String.format("DataLoader - Get CSV in classpath %s", filePath));

        	bookService.uploadBooksByCSV(filePath);
        }
    }    
    
    private boolean isValidFilePath(String filePath) {
    	if (filePath != null && !filePath.trim().isEmpty()) {
            File file = new File(filePath.trim());
            return EXPECTED_FILE_NAME.equals(file.getName());
        }
        return false;
    }

    private boolean validateCSV(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        if (lines.isEmpty()) {
            return false;
        }

        String[] columns = lines.get(0).split(",");
        return expectedColumns.equals(Arrays.asList(columns));
    }

}