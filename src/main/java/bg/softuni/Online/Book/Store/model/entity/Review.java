package bg.softuni.Online.Book.Store.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private int rating;

    private String comment;

    private LocalDate created;

    public Review() {
    }

    public User getUser() {
        return user;
    }

    public Review setUser(User user) {
        this.user = user;
        return this;
    }

    public Book getBook() {
        return book;
    }

    public Review setBook(Book book) {
        this.book = book;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public Review setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Review setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public Review setCreated(LocalDate created) {
        this.created = created;
        return this;
    }
}
