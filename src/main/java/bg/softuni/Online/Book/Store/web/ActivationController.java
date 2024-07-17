package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ActivationController {

    private final UserService userService;

    public ActivationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/activate")
    public ModelAndView activateAccount(@RequestParam("token") String token) {
        ModelAndView modelAndView = new ModelAndView();
        boolean isActivated = userService.activateAccount(token);
        if (isActivated) {
            modelAndView.setViewName("redirect:/users/login?activationSuccess");
        } else {
            modelAndView.setViewName("redirect:/users/login?activationFailure");
        }
        return modelAndView;
    }

    @PostMapping("/requestActivationByEmail")
    public ModelAndView requestActivationEmail(@RequestParam String email) {
        boolean isValid = userService.requestActivationEmail(email);

        if (!isValid) {
            return new ModelAndView("redirect:/users/login?emailNotExists");
        }
        return new ModelAndView("redirect:/users/login?emailSendSuccessful");
    }
}
