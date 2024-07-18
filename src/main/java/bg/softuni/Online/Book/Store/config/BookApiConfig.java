package bg.softuni.Online.Book.Store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "books.api")
public class BookApiConfig {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public BookApiConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
}
