package bg.softuni.Online.Book.Store.model.dto.review;

import java.time.LocalDate;

public class ReviewViewDTO {
    private String userUsername;

    private String comment;

    private int rating;

    private LocalDate created;

    public ReviewViewDTO() {}

    public String getUserUsername() {
        return userUsername;
    }

    public ReviewViewDTO setUserUsername(String userUsername) {
        this.userUsername = userUsername;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ReviewViewDTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public ReviewViewDTO setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public ReviewViewDTO setCreated(LocalDate created) {
        this.created = created;
        return this;
    }
}
