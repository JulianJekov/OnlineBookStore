package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.dto.review.ReviewCommentDTO;
import bg.softuni.Online.Book.Store.model.dto.review.ReviewDTO;
import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.model.entity.Review;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.repository.ReviewRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static bg.softuni.Online.Book.Store.constants.Exceptions.USER_NOT_FOUND;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean createReview(ReviewDTO reviewDTO, BookStoreUserDetails userDetails) {
        String bookTitle = reviewDTO.getBookTitle();
        Long id = userDetails.getId();

        User user = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
        Book book = bookRepository.findByTitle(bookTitle).orElse(null);

        if (book == null) {
            return false;
        }

        Review review = new Review();
        review.setBook(book);
        review.setCreated(LocalDate.now());
        review.setComment(reviewDTO.getComment());
        review.setUser(user);
        reviewRepository.save(review);
        return true;
    }

    @Override
    public List<ReviewCommentDTO> getBookReviews(Long id) {
        return reviewRepository.findByBookId(id)
                .stream()
                .map(review -> modelMapper.map(review, ReviewCommentDTO.class))
                .toList();
    }
}
