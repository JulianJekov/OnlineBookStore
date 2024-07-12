package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.review.ReviewDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;

public interface ReviewService {
    void createReview(ReviewDTO reviewDTO, BookStoreUserDetails userDetails);

    void delete(Long id);
}
