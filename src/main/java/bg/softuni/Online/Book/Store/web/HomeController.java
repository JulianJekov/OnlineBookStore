package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails instanceof BookStoreUserDetails bookStoreUserDetails) {
            model.addAttribute("welcome", bookStoreUserDetails.getFullName());
        } else {
            model.addAttribute("welcome", "Anonymous");
        }
        return "home";
    }
}
