package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.dto.review.ReviewDTO;
import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.model.entity.Review;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.repository.ReviewRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {


    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewServiceImpl reviewServiceImpl;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateReviewUserNotFoundShouldThrowException() {
        ReviewDTO reviewDTO = new ReviewDTO();
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> reviewServiceImpl.createReview(reviewDTO, id));
        verify(userRepository).findById(id);
    }

    @Test
    void testCreateReviewBookNotFoundShouldThrowException() {
        Long id = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(id);
        Long bookId = reviewDTO.getBookId();

        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> reviewServiceImpl.createReview(reviewDTO, id));
        verify(userRepository).findById(id);
        verify(bookRepository).findById(bookId);
    }

    @Test
    void testCreateReviewShouldCreateReview() {
        Long id = 1L;
        ReviewDTO reviewDTO = createReviewDTO();
        reviewDTO.setBookId(id);

        User user = new User();
        user.setId(id);

        Book book = new Book();
        book.setId(reviewDTO.getBookId());

        Review review = new Review();
        review.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(bookRepository.findById(reviewDTO.getBookId())).thenReturn(Optional.of(book));
        when(modelMapper.map(reviewDTO, Review.class)).thenReturn(review);
        reviewServiceImpl.createReview(reviewDTO, id);

        verify(userRepository).findById(id);
        verify(bookRepository).findById(reviewDTO.getBookId());
        verify(modelMapper).map(reviewDTO, Review.class);
        verify(reviewRepository).save(review);

        assertEquals(user, review.getUser());
        assertEquals(book, review.getBook());
    }

    @Test
    void testDeleteShouldDeleteReview() {
        Long id = 1L;

        reviewServiceImpl.delete(id);

        verify(reviewRepository).deleteById(id);
    }

    private static ReviewDTO createReviewDTO() {
        return new ReviewDTO()
                .setId(1L)
                .setBookId(1L)
                .setCreated(LocalDate.now())
                .setComment("test")
                .setRating(1);
    }

}