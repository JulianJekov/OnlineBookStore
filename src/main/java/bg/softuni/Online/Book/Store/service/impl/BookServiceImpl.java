package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.dto.book.AddBookDTO;
import bg.softuni.Online.Book.Store.model.dto.book.AllBooksDTO;
import bg.softuni.Online.Book.Store.model.dto.book.BookDetailsDTO;
import bg.softuni.Online.Book.Store.model.dto.book.EditBookDTO;
import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.repository.CartItemRepository;
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
    private final CartItemRepository cartItemRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           ModelMapper modelMapper,
                           ImageCloudService imageCloudService, CartItemRepository cartItemRepository) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.imageCloudService = imageCloudService;
        this.cartItemRepository = cartItemRepository;
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
        Book book = modelMapper.map(editBookDTO, Book.class);
        book.setImageUrl(imageCloudService.uploadImg(editBookDTO.getImageUrl()));
        bookRepository.save(book);

        return book.getId();
    }

    @Override
    public Optional<Book> findByISBN(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        cartItemRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
        //TODO: handle the reviews that book will have also books will be bought by user also hand this
    }
}
