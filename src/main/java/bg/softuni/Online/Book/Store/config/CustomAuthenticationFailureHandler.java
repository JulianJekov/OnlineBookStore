package bg.softuni.Online.Book.Store.config;

import bg.softuni.Online.Book.Store.exceptions.UserNotActivatedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception.getCause() instanceof UserNotActivatedException || exception instanceof UserNotActivatedException) {
            request.setAttribute("not_active", true);
            request.getRequestDispatcher("/users/login-error").forward(request, response);
        } else {
            request.setAttribute("bad_credentials", true);
            request.getRequestDispatcher("/users/login-error").forward(request, response);
        }
    }
}
