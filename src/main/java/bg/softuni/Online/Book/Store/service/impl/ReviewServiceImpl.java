package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.dto.review.ReviewDTO;
import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.model.entity.Review;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.repository.ReviewRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import static bg.softuni.Online.Book.Store.constants.Exceptions.*;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             BookRepository bookRepository,
                             ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createReview(ReviewDTO reviewDTO,Long id) {
        Long bookId = reviewDTO.getBookId();

        User user = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ObjectNotFoundException(String.format(BOOK_NOT_FOUND, bookId)));

        Review review = modelMapper.map(reviewDTO, Review.class);
        review.setUser(user);
        review.setBook(book);
        reviewRepository.save(review);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}
