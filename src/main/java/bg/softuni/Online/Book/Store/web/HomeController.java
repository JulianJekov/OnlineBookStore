package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.book.TopRatedBookDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.service.BookService;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class HomeController {

    private final MessageSource messageSource;
    private final BookService bookService;

    public HomeController(MessageSource messageSource, BookService bookService) {
        this.messageSource = messageSource;
        this.bookService = bookService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about");
    }

    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails, Locale locale) {

        ModelAndView modelAndView = new ModelAndView("home");

        if (userDetails instanceof BookStoreUserDetails bookStoreUserDetails) {
            modelAndView.addObject("welcome", bookStoreUserDetails.getFullName());
        } else {
            modelAndView.addObject("welcome", "Anonymous");
        }
        TopRatedBookDTO topRated = bookService.getTopRated();

        if (topRated == null) {
            modelAndView.addObject("no_reviews",
                    messageSource.getMessage("home_no_reviews", null, locale));
        }
        modelAndView.addObject("topRatedBook", topRated);
        return modelAndView;
    }
}
