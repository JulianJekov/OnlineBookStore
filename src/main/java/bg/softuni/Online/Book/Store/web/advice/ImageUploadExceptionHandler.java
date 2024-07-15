package bg.softuni.Online.Book.Store.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ImageUploadExceptionHandler {

    //Not working for some reason
    //When debugging it stops here but not handling the exception
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMax(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "File is too large.");
        return "error/upload-error";
    }
}
