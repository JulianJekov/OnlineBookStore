package bg.softuni.Online.Book.Store.model.dto.book;

public class TopRatedBookDTO {
    private Long id;

    private String title;

    private String author;

    private String imageUrl;

    private double averageRating;

    private int reviewCount;

    public TopRatedBookDTO() {
    }


    public Long getId() {
        return id;
    }

    public TopRatedBookDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TopRatedBookDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public TopRatedBookDTO setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
        return this;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public TopRatedBookDTO setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public TopRatedBookDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public TopRatedBookDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
