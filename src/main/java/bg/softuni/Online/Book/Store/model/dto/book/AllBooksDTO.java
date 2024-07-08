package bg.softuni.Online.Book.Store.model.dto.book;

public class AllBooksDTO {

    private Long id;

    private String title;

    private String author;

    private String imageUrl;

    public AllBooksDTO() {}

    public Long getId() {
        return id;
    }

    public AllBooksDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AllBooksDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public AllBooksDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public AllBooksDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
