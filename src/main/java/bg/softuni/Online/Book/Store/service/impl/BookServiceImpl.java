package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.model.dto.book.AddBookDTO;
import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.service.BookService;
import bg.softuni.Online.Book.Store.util.ISBNUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
