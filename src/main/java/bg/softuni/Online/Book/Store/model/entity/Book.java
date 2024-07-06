package bg.softuni.Online.Book.Store.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToMany(mappedBy = "boughtBooks")
    private List<User> boughtBy = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Review> reviews = new ArrayList<>();

    public Book() {}

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public Book setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Book setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public List<User> getBoughtBy() {
        return boughtBy;
    }

    public Book setBoughtBy(List<User> boughtBy) {
        this.boughtBy = boughtBy;
        return this;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Book setReviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Book setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
