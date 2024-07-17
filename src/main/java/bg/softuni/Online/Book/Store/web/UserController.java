package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.exceptions.FieldError;
import bg.softuni.Online.Book.Store.exceptions.ValidationException;
import bg.softuni.Online.Book.Store.model.dto.user.ChangePasswordDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserProfileDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserRegisterDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

        return new ModelAndView("redirect:/users/login?confirmEmail");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("/login");
    }

    @PostMapping("/login-error")
    public ModelAndView onFailure(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/login");
        if (request.getAttribute("not_active") != null) {
            modelAndView.addObject("not_activated", true);
        } else if (request.getAttribute("bad_credentials") != null) {
            modelAndView.addObject("bad_credentials", true);
        }
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

    @GetMapping("/edit-profile")
    public ModelAndView editProfile(@AuthenticationPrincipal BookStoreUserDetails userDetails) {
        ModelAndView modelAndView = new ModelAndView("/edit-profile");
        Long id = userDetails.getId();
        UserProfileDTO userProfileDTO = userService.getUserDetails(id);
        if (!modelAndView.getModel().containsKey("userProfileDTO")) {
            modelAndView.addObject("userProfileDTO", userProfileDTO);
        }
        return modelAndView;
    }

    @PatchMapping("/edit-profile")
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

    @GetMapping("/change-password")
    public ModelAndView changePassword() {
        return new ModelAndView("/change-password");
    }

    @PatchMapping("/change-password")
    public ModelAndView changePassword(@AuthenticationPrincipal BookStoreUserDetails userDetails,
                                           @Valid ChangePasswordDTO changePasswordDTO,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        Long id = userDetails.getId();
        try {
            userService.validateOldPassword(id, changePasswordDTO);
        } catch (ValidationException e) {
            for (FieldError fieldError : e.getFieldErrors()) {
                bindingResult.rejectValue(
                        fieldError.getFieldName(),
                        "error." + fieldError.getFieldName(),
                        fieldError.getErrorMessage());
            }
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("changePasswordDTO", changePasswordDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.changePasswordDTO", bindingResult);
            return new ModelAndView("redirect:/users/change-password");
        }
        userService.changePassword(id, changePasswordDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return new ModelAndView("redirect:/users/login?passwordChangeSuccess");
    }
}
