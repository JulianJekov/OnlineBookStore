package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.FieldError;
import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.exceptions.ValidationException;
import bg.softuni.Online.Book.Store.model.dto.book.*;
import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.model.entity.Review;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static bg.softuni.Online.Book.Store.constants.Exceptions.BOOK_NOT_FOUND;
import static bg.softuni.Online.Book.Store.constants.ValidationMessages.BOOK_ISBN_EXISTS;
import static bg.softuni.Online.Book.Store.constants.ValidationMessages.BOOK_TITLE_EXISTS_MESSAGE;

@Service
public class BookServiceImpl implements BookService {

    private static final String TITLE = "title";
    private static final String ISBN = "isbn";

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final ImageCloudService imageCloudService;
    private final RestClient bookRestClient;

    public BookServiceImpl(BookRepository bookRepository,
                           ModelMapper modelMapper,
                           ImageCloudService imageCloudService,
                           RestClient bookRestClient) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.imageCloudService = imageCloudService;
        this.bookRestClient = bookRestClient;
    }

    @Override
    @Transactional
    public void createBook(AddBookDTO addBookDTO) throws IOException {

        MultiValueMap<String, Object> body = constructMultipartFormData(addBookDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        bookRestClient
                .post()
                .uri("/books/add")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(body)
                .retrieve()
                .body(AddBookDTO.class);
    }


    @Override
    public Page<AllBooksDTO> findAllBooks(Pageable pageable) {

        PageResponse<AllBooksDTO> books = bookRestClient
                .get()
                .uri("/books/all?page={page}&size={size}&sort=id",
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSort())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        assert books != null;

        return new PageImpl<>(books.getContent(), pageable, books.getPage().totalElements());
    }

    @Override
    public BookDetailsDTO findBookById(Long id) {

        return bookRestClient
                .get()
                .uri("/books/details/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(BookDetailsDTO.class);

    }

    @Override
    public EditBookDTO findBookByIdEdit(Long id) {
        return bookRepository.findById(id).map(book -> modelMapper.map(book, EditBookDTO.class))
                .orElseThrow(() -> new ObjectNotFoundException(String.format(BOOK_NOT_FOUND, id)));
    }


    @Override
    public void validateEditBook(EditBookDTO editBookDTO) {
        Long id = editBookDTO.getId();
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format(BOOK_NOT_FOUND, id))
        );
        String editedBookTitle = editBookDTO.getTitle();
        String currentBookTitle = existingBook.getTitle();

        String editedBookISBN = editBookDTO.getIsbn();
        String currentBookISBN = existingBook.getIsbn();

        List<FieldError> errors = new ArrayList<>();
        if (!currentBookTitle.equals(editedBookTitle) && bookRepository.findByTitle(editedBookTitle).isPresent()) {
            errors.add(new FieldError(TITLE, BOOK_TITLE_EXISTS_MESSAGE));
        }

        if (!currentBookISBN.equals(editedBookISBN) && bookRepository.findByIsbn(editedBookISBN).isPresent()) {
            errors.add(new FieldError(ISBN, BOOK_ISBN_EXISTS));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

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
    public TopRatedBookDTO getTopRated() {
        Book book = bookRepository.findTopRatedBook().orElse(null);

        if (book == null) {
            return null;
        }

        double averageRating = book.getReviews().stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        int reviewCount = book.getReviews().size();

        TopRatedBookDTO topRatedBookDTO = modelMapper.map(book, TopRatedBookDTO.class);
        topRatedBookDTO.setAverageRating(averageRating);
        topRatedBookDTO.setReviewCount(reviewCount);

        return topRatedBookDTO;
    }


    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRestClient
                .delete()
                .uri("/books/delete/{id}", id)
                .retrieve();
    }

    private static MultiValueMap<String, Object> constructMultipartFormData(AddBookDTO addBookDTO) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", addBookDTO.getTitle());
        body.add("author", addBookDTO.getAuthor());
        body.add("publisher", addBookDTO.getPublisher());
        body.add("price", addBookDTO.getPrice());

        body.add("imageUrl", new ByteArrayResource(addBookDTO.getImageUrl().getBytes()) {
            @Override
            public String getFilename() {
                return addBookDTO.getImageUrl().getOriginalFilename();
            }
        });
        return body;
    }
}
