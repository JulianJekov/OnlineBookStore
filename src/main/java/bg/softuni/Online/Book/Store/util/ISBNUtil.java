package bg.softuni.Online.Book.Store.util;

import java.security.SecureRandom;

public class ISBNUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateISBN() {
        StringBuilder isbn = new StringBuilder("978");
        for (int i = 0; i < 10; i++) {
            int number = RANDOM.nextInt(10);
            isbn.append(number);
        }
        return isbn.toString();
    }

}
