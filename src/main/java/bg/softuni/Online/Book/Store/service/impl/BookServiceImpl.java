package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.dto.book.*;
import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.model.entity.Review;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.service.BookService;
import bg.softuni.Online.Book.Store.util.ISBNUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static bg.softuni.Online.Book.Store.constants.Exceptions.BOOK_NOT_FOUND;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final ImageCloudService imageCloudService;

    public BookServiceImpl(BookRepository bookRepository,
                           ModelMapper modelMapper,
                           ImageCloudService imageCloudService) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.imageCloudService = imageCloudService;
    }

    @Override
    @Transactional
    public Long createBook(AddBookDTO addBookDTO) {
        Book book = modelMapper.map(addBookDTO, Book.class);
        book.setIsbn(ISBNUtil.generateISBN());
        book.setImageUrl(imageCloudService.uploadImg(addBookDTO.getImageUrl()));
        return bookRepository.save(book).getId();
    }

    @Override
    public Page<AllBooksDTO> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(book -> modelMapper.map(book, AllBooksDTO.class));
    }

    @Override
    public BookDetailsDTO findBookById(Long id) {
        return bookRepository.findById(id).map(book -> modelMapper.map(book, BookDetailsDTO.class))
                .orElseThrow(() -> new ObjectNotFoundException(String.format(BOOK_NOT_FOUND, id)));
    }

    @Override
    public EditBookDTO findBookByIdEdit(Long id) {
        return bookRepository.findById(id).map(book -> modelMapper.map(book, EditBookDTO.class))
                .orElseThrow(() -> new ObjectNotFoundException(String.format(BOOK_NOT_FOUND, id)));
    }

    @Override
    public Long editBook(EditBookDTO editBookDTO) {
        Long id = editBookDTO.getId();
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format(BOOK_NOT_FOUND, id))
        );
        modelMapper.map(editBookDTO, existingBook);
        existingBook.setImageUrl(imageCloudService.uploadImg(editBookDTO.getImageUrl()));
        bookRepository.save(existingBook);

        return existingBook.getId();
    }

    @Override
    public Optional<Book> findByISBN(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
        //TODO: handle the reviews that book will have
        //TODO: test orphanRemoval
    }
}
