package bg.softuni.Online.Book.Store.model.dto.review;

public class ReviewCommentDTO {
    private String comment;

    public ReviewCommentDTO() {
    }

    public String getComment() {
        return comment;
    }

    public ReviewCommentDTO setComment(String comment) {
        this.comment = comment;
        return this;
    }
}
