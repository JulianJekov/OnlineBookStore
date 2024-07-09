USE
`book-store`;

INSERT INTO roles (id, name)
VALUES (1, 'ADMIN');
INSERT INTO roles (id, name)
VALUES (2, 'USER');


INSERT INTO users (id, email, password, username, first_name, last_name, age, is_active)
VALUES (1, 'admin@admin.com',
        'afdbcdb98a3dae8cbd1bd09078760e8496d7a175fa727e699399a1d467fc9b4a21e7dac5711bff876bd9d5541e56280f',
        'admin', 'Admin', 'Adminov', 30, true);

INSERT INTO users (id, email, password, username, first_name, last_name, age, is_active)
VALUES (2, 'user@user.com',
        'afdbcdb98a3dae8cbd1bd09078760e8496d7a175fa727e699399a1d467fc9b4a21e7dac5711bff876bd9d5541e56280f',
        'user', 'User', 'Userov', 31, true);

-- user roles
-- admin
INSERT INTO users_roles (`user_id`, `roles_id`)
VALUES (1, 1);
INSERT INTO users_roles (`user_id`, `roles_id`)
VALUES (1, 2);
-- user
INSERT INTO users_roles (`user_id`, `roles_id`)
VALUES (2, 2);

INSERT INTO shopping_cards (id, user_id)
VALUES (1, 1);
INSERT INTO shopping_cards (id, user_id)
VALUES (2, 2);

INSERT INTO books (title, author, isbn, publisher, price, image_url)
VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Charles Scribner & Sons', 10.99,
        'https://i.insider.com/518296d969beddd06d000001?width=640&format=jpeg'),
       ('1984', 'George Orwell', '9780451524935', 'Secker & Warburg', 9.99,
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRYBv3FfhbO20JpyaTb85kmYyPhIoL77LA_Q&s'),
       ('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'J.B. Lippincott & Co.', 7.99,
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTaWMv-4T5o8cPe0YzwHyVYhc9-H-Ts3ZItkQ&s'),
       ('Pride and Prejudice', 'Jane Austen', '9780141439518', 'T. Egerton, Whitehall', 5.99,
        'https://s2982.pcdn.co/wp-content/uploads/2018/10/pride-and-prejudice-barnes-and-noble-and-race-point.png'),
       ('Moby-Dick', 'Herman Melville', '9781503280786', 'Harper & Brothers', 12.99,
        'https://pictures.abebooks.com/inventory/md/md31517870402.jpg'),
       ('The Catcher in the Rye', 'J.D. Salinger', '9780316769488', 'Little, Brown and Company', 8.99,
        'https://images.penguinrandomhouse.com/cover/9780316769488'),
       ('Brave New World', 'Aldous Huxley', '9780060850524', 'Chatto & Windus', 10.49,
        'https://prodimage.images-bn.com/pimages/9780060850524_p0_v4_s1200x630.jpg'),
       ('The Hobbit', 'J.R.R. Tolkien', '9780547928227', 'George Allen & Unwin', 14.95,
        'https://images-na.ssl-images-amazon.com/images/I/91b0C2YNSrL.jpg'),
       ('Fahrenheit 451', 'Ray Bradbury', '9781451673319', 'Ballantine Books', 11.99,
        'https://images-na.ssl-images-amazon.com/images/I/81p%2Bxe8cbnL.jpg'),
       ('The Adventures of Huckleberry Finn', 'Mark Twain', '9780486280615', 'Charles L. Webster And Company', 4.99,
        'https://m.media-amazon.com/images/I/91Suc5Kql8L._AC_UF1000,1000_QL80_.jpg');