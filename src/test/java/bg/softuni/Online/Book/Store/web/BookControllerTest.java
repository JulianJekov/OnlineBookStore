package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.service.BookService;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @MockBean
    private Cloudinary cloudinary;

    private Book book;

    @BeforeEach
    void setUp() throws IOException {
        bookRepository.deleteAll();
        book = new Book()
                .setTitle("test title")
                .setAuthor("test author")
                .setPrice(BigDecimal.valueOf(100.0))
                .setIsbn("ISBN")
                .setPublisher("Publisher")
                .setImageUrl("http://example.com/test.jpg");
        bookRepository.save(book);

        Uploader uploaderMock = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploaderMock);
        when(uploaderMock.upload(any(), any())).thenReturn(
                Collections.singletonMap("url", "http://example.com/image.jpg"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddBookGetEndpoint() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("/add-book"));
    }

    @Test
    @WithMockUser(username = "user")
    void testAddBookGetEndpointForbidden() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testEditBookGetEndpoint() throws Exception {
        mockMvc.perform(get("/books/edit/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/edit-book"))
                .andExpect(model().attributeExists("editBookDTO"));
    }

    @Test
    @WithMockUser(username = "user")
    void testEditBookGetEndpointForbidden() throws Exception {
        mockMvc.perform(get("/books/edit/{id}", book.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testEditBookPatchEndpoint() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "imageUrl",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes());

        mockMvc.perform(multipart("/books/edit/{id}", book.getId())
                        .file(file)
                        .param("title", "Updated Title")
                        .param("author", "Updated Author")
                        .param("isbn", "1234567890123")
                        .param("publisher", "Updated Publisher")
                        .param("price", "29.99")
                        .contentType("multipart/form-data")
                        .with(csrf())
                        .with(request ->
                        {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/details/" + book.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testEditBookPatchEndpointInvalidData() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "imageUrl",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes());

        mockMvc.perform(multipart("/books/edit/{id}", book.getId())
                        .file(file)
                        .param("title", "t")
                        .param("author", "")
                        .param("isbn", "123456789012")
                        .param("publisher", "Publisher")
                        .param("price", "-1.00")
                        .contentType("multipart/form-data")
                        .with(csrf())
                        .with(request ->
                        {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().attributeExists("editBookDTO"))
                .andExpect(model().attributeHasFieldErrors("editBookDTO", "title", "author", "isbn", "price"));
    }
}