package bg.softuni.Online.Book.Store.model.dto.book;

import java.math.BigDecimal;

public class BookDetailsDTO {

    private Long id;

    private String title;

    private String author;

    private String publisher;

    private String imageUrl;

    private String isbn;

    private BigDecimal price;

    public BookDetailsDTO() {}

    public String getTitle() {
        return title;
    }

    public BookDetailsDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public BookDetailsDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public BookDetailsDTO setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookDetailsDTO setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BookDetailsDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public BookDetailsDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Long getId() {
        return id;
    }

    public BookDetailsDTO setId(Long id) {
        this.id = id;
        return this;
    }
}
