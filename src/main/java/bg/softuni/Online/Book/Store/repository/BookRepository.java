package bg.softuni.Online.Book.Store.repository;

import bg.softuni.Online.Book.Store.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

    Optional<Book> findByTitleAndIdNot(String title, Long id);

    Optional<Book> findByIsbn(String isbn);

    @Query(value = """ 
                        SELECT b.*
                        FROM books b
                        JOIN reviews r ON b.id = r.book_id
                        GROUP BY b.id
                        HAVING AVG(r.rating) = (SELECT MAX(avg_rating)
                        FROM (SELECT AVG(r.rating) AS avg_rating
                        FROM reviews r
                        GROUP BY r.book_id) AS avg_ratings)
                        LIMIT 1
                        """,
            nativeQuery = true)
    Optional<Book> findTopRatedBook();
}
