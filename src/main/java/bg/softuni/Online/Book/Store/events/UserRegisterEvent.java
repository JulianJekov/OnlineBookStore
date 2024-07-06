package bg.softuni.Online.Book.Store.events;

import bg.softuni.Online.Book.Store.model.entity.User;
import org.springframework.context.ApplicationEvent;

public class UserRegisterEvent extends ApplicationEvent {

    private final User user;

    public UserRegisterEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
