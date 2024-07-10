package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.review.ReviewDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/review")
    public ModelAndView review() {
        return new ModelAndView("/review");
    }

    @PostMapping("/review")
    public ModelAndView reviewSubmit(@Valid ReviewDTO reviewDTO, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     @AuthenticationPrincipal BookStoreUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("/review");
        }

        boolean isCreated = reviewService.createReview(reviewDTO, userDetails);

        if (!isCreated) {
            return new ModelAndView("/review", "bad_credentials", true);
        }

        return new ModelAndView("redirect:/home");
    }

    @ModelAttribute("reviewDTO")
    public ReviewDTO reviewDTO() {
        return new ReviewDTO();
    }

}
