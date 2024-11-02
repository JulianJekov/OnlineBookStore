package bg.softuni.Online.Book.Store.config;

import bg.softuni.Online.Book.Store.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource,
                                                       RoleRepository roleRepository,
                                                       ResourceLoader resourceLoader) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        if (roleRepository.count() == 0) {
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
            databasePopulator.addScript(resourceLoader.getResource("classpath:data.sql"));
            initializer.setDatabasePopulator(databasePopulator);
        }
        return initializer;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
