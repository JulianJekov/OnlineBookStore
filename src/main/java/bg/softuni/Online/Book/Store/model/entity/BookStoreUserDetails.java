package bg.softuni.Online.Book.Store.model.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class BookStoreUserDetails extends User {

    private final Long id;
    private final String firstName;
    private final String lastName;

    public BookStoreUserDetails(
            String username, String password,
            Collection<? extends GrantedAuthority> authorities, Long id,
            String firstName,
            String lastName) {
        super(username, password, authorities);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();

        if (firstName != null) {
            sb.append(firstName);
        }
        if (lastName != null) {
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append(lastName);
        }
        return sb.toString();
    }

}
