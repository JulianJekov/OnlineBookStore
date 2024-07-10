package bg.softuni.Online.Book.Store.model.dto.review;

public class ReviewDTO {
    private String comment;
    private String bookTitle;

    public ReviewDTO() {
    }

    public String getComment() {
        return comment;
    }

    public ReviewDTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public ReviewDTO setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }
}
