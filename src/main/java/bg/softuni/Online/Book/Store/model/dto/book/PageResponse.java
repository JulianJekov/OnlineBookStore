package bg.softuni.Online.Book.Store.model.dto.book;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.web.PagedModel;

import java.util.List;

public class PageResponse<T> {
    private List<T> content;
    private PagedModel.PageMetadata page;

    @NotNull
    public List<T> getContent() {
        return content != null ? content : List.of();
    }

    public PageResponse<T> setContent(List<T> content) {
        this.content = content;
        return this;
    }

    public PagedModel.PageMetadata getPage() {
        return page;
    }

    public PageResponse<T> setPage(PagedModel.PageMetadata page) {
        this.page = page;
        return this;
    }
}
