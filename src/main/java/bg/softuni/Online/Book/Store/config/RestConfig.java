package bg.softuni.Online.Book.Store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

    @Bean
    public RestClient bookRestClient(BookApiConfig bookApiConfig) {
        return RestClient
                .builder()
                .baseUrl(bookApiConfig.getBaseUrl())
                .defaultHeader(MediaType.MULTIPART_FORM_DATA_VALUE)
                .build();
    }

}
