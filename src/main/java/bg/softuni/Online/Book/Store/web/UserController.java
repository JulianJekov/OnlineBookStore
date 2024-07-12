package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.exceptions.FieldError;
import bg.softuni.Online.Book.Store.exceptions.ValidationException;
import bg.softuni.Online.Book.Store.model.dto.user.ChangePasswordDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserProfileDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserRegisterDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @ModelAttribute("changePasswordDTO")
    public ChangePasswordDTO changePasswordDTO() {
        return new ChangePasswordDTO();
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

    @GetMapping("/profile")
    public ModelAndView profile(@AuthenticationPrincipal BookStoreUserDetails userDetails) {
        Long id = userDetails.getId();
        ModelAndView modelAndView = new ModelAndView("/profile");
        UserProfileDTO userProfileDTO = userService.getUserDetails(id);
        modelAndView.addObject("userProfileDTO", userProfileDTO);
        return modelAndView;
    }

    @GetMapping("/edit-profile/{id}")
    public ModelAndView editProfile(@PathVariable("id") Long id, @AuthenticationPrincipal BookStoreUserDetails userDetails) {
        ModelAndView modelAndView = new ModelAndView("/edit-profile");
        UserProfileDTO userProfileDTO = userService.getUserDetails(id);
        if (!modelAndView.getModel().containsKey("userProfileDTO")) {
            modelAndView.addObject("userProfileDTO", userProfileDTO);
        }
        return modelAndView;
    }

    @PatchMapping("/edit-profile/{id}")
    public ModelAndView editProfile(@Valid UserProfileDTO userProfileDTO, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        try {
            userService.validateUserProfile(userProfileDTO);
        } catch (ValidationException e) {
            for (FieldError fieldError : e.getFieldErrors()) {
                bindingResult.rejectValue(fieldError.getFieldName(), "error." + fieldError.getFieldName(), fieldError.getErrorMessage());
            }
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userProfileDTO", userProfileDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userProfileDTO", bindingResult);
            return new ModelAndView("edit-profile");
        }

        userService.editProfile(userProfileDTO);

        return new ModelAndView("/profile");
    }

    @GetMapping("/change-password/{id}")
    public ModelAndView changePassword(@PathVariable("id") Long id) {
        return new ModelAndView("/change-password");
    }
}
