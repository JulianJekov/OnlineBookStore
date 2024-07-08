package bg.softuni.Online.Book.Store.model.dto.book;

import bg.softuni.Online.Book.Store.validations.imageValidation.ImageFileAnnotation;
import bg.softuni.Online.Book.Store.validations.uniqueBookTitle.UniqueBookTitle;
import bg.softuni.Online.Book.Store.validations.uniqueISBN.UniqueISBN;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static bg.softuni.Online.Book.Store.constants.ValidationMessages.*;

public class EditBookDTO {

    private Long id;

    @Size(min = 2, max = 50, message = BOOK_TITLE_LENGTH_MESSAGE)
    @NotNull(message = BOOK_TITLE_CAN_NOT_BE_NULL)
    @UniqueBookTitle
    private String title;

    @Size(min = 3, max = 50, message = BOOK_AUTHOR_LENGTH_MESSAGE)
    @NotNull(message = BOOK_AUTHOR_CAN_NOT_BE_NULL)
    private String author;

    @Size(min = 3, max = 50, message = BOOK_PUBLISHER_LENGTH_MESSAGE)
    @NotNull(message = BOOK_PUBLISHER_CAN_NOT_BE_NULL)
    private String publisher;

    @UniqueISBN
    @Size(min = 13, max = 13, message = BOOK_ISBN_SIZE)
    @NotNull(message = BOOK_ISBN_CAN_NOT_BE_NULL)
    private String isbn;

    @Positive(message = BOOK_PRICE_POSITIVE)
    @NotNull(message = BOOK_PRICE_POSITIVE)
    private BigDecimal price;

    @ImageFileAnnotation(contentTypes = {"image/png", "image/jpeg"})
    private MultipartFile imageUrl;


    public EditBookDTO() {
    }

    public String getTitle() {
        return title;
    }

    public EditBookDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public EditBookDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public EditBookDTO setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public EditBookDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public MultipartFile getImageUrl() {
        return imageUrl;
    }

    public EditBookDTO setImageUrl(MultipartFile imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public EditBookDTO setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public Long getId() {
        return id;
    }

    public EditBookDTO setId(Long id) {
        this.id = id;
        return this;
    }
}
