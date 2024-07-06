package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.book.AddBookDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BookService {
    Long createBook(AddBookDTO addBookDTO) throws IOException;
}
