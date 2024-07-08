package bg.softuni.Online.Book.Store.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReviewController {

    @GetMapping("/review")
    public ModelAndView review() {
        return new ModelAndView("/review");
    }

}
