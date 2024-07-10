package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.review.ReviewCommentDTO;
import bg.softuni.Online.Book.Store.model.dto.review.ReviewDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;

import java.util.List;

public interface ReviewService {
    boolean createReview(ReviewDTO reviewDTO, BookStoreUserDetails userDetails);

    List<ReviewCommentDTO> getBookReviews(Long id);
}
