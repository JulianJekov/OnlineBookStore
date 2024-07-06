package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.UserRegisterDTO;
import bg.softuni.Online.Book.Store.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userRegisterDTO")
    public UserRegisterDTO userRegisterDTO() {
        return new UserRegisterDTO();
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register");
    }

    @PostMapping("/register")

    public ModelAndView register(@Valid UserRegisterDTO userRegisterDTO, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterDTO", userRegisterDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userRegisterDTO", bindingResult);
            return new ModelAndView("redirect:/users/register");
        }

        userService.register(userRegisterDTO);

        return new ModelAndView("redirect:/users/login");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("/login");
    }

    @PostMapping("/login-error")
    public ModelAndView onFailure() {
        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject("bad_credentials", true);
        return modelAndView;
    }
}
