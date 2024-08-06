package bg.softuni.Online.Book.Store.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ImageUploadExceptionHandler {

    //Not working for some reason
    //When debugging it stops here but not handling the exception

    //Work when the app is deployed
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMax() {
        return new ModelAndView("error/upload-error");
    }
}
