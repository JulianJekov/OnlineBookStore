package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.review.ReviewDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ModelAndView reviewSubmit(ReviewDTO reviewDTO,
                                     @AuthenticationPrincipal BookStoreUserDetails userDetails) {
        reviewService.createReview(reviewDTO, userDetails);
        return new ModelAndView("redirect:/books/details/" + reviewDTO.getBookId());
    }
}
