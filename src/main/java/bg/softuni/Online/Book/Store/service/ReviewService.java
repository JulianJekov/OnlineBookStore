package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.review.ReviewDTO;

public interface ReviewService {
    void createReview(ReviewDTO reviewDTO,Long id);

    void delete(Long id);
}
