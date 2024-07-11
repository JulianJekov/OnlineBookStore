package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.book.TopRatedBookDTO;
import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.service.BookService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final BookService bookService;

    public HomeController(BookService bookService) {
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
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails) {

        ModelAndView modelAndView = new ModelAndView("home");

        if (userDetails instanceof BookStoreUserDetails bookStoreUserDetails) {
            modelAndView.addObject("welcome", bookStoreUserDetails.getFullName());
        } else {
            modelAndView.addObject("welcome", "Anonymous");
        }

        modelAndView.addObject("topRatedBook", bookService.getTopRated());

        return modelAndView;
    }
}
