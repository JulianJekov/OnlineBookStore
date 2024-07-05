package bg.softuni.Online.Book.Store.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private int age;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_books", joinColumns =
    @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns =
    @JoinColumn(name = "book_id", referencedColumnName = "id"))
    private List<Book> boughtBooks = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public User setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public User setActive(boolean active) {
        isActive = active;
        return this;
    }

    private LocalDate lastLoginDate;

    private boolean isActive;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public List<Book> getBoughtBooks() {
        return boughtBooks;
    }

    public User setBoughtBooks(List<Book> boughtBooks) {
        this.boughtBooks = boughtBooks;
        return this;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public User setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        return this;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public User setReviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }
}
