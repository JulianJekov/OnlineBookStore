package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.book.*;
import bg.softuni.Online.Book.Store.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Optional;

public interface BookService {
    void createBook(AddBookDTO addBookDTO) throws IOException;
    //TODO:

    Page<AllBooksDTO> findAllBooks(Pageable pageable);

    BookDetailsDTO findBookById(Long id);

    EditBookDTO findBookByIdEdit(Long id);

    Long editBook(EditBookDTO editBookDTO);

    Optional<Book> findByISBN(String isbn);

    void deleteBook(Long id);

    TopRatedBookDTO getTopRated();
}
