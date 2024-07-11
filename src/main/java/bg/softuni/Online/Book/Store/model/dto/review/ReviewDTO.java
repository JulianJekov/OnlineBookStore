package bg.softuni.Online.Book.Store.model.dto.review;

import java.time.LocalDate;

public class ReviewDTO {
    private Long id;
    private Long bookId;
    private int rating;
    private String comment;
    private LocalDate created = LocalDate.now();

    public ReviewDTO() {
    }

    public Long getId() {
        return id;
    }

    public ReviewDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getBookId() {
        return bookId;
    }

    public ReviewDTO setBookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public ReviewDTO setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ReviewDTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public ReviewDTO setCreated(LocalDate created) {
        this.created = created;
        return this;
    }
}
