package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.book.AddBookDTO;
import bg.softuni.Online.Book.Store.model.dto.book.AllBooksDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BookService {
    Long createBook(AddBookDTO addBookDTO) throws IOException;

    Page<AllBooksDTO> findAllBooks(Pageable pageable);
}
