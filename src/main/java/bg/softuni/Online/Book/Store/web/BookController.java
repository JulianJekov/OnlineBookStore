package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.book.AddBookDTO;
import bg.softuni.Online.Book.Store.model.dto.book.AllBooksDTO;
import bg.softuni.Online.Book.Store.model.dto.book.BookDetailsDTO;
import bg.softuni.Online.Book.Store.model.dto.book.EditBookDTO;
import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/add")
    public ModelAndView addBook() {
        return new ModelAndView("/add-book");
    }

    @PostMapping("/add")
    public ModelAndView addBook(@Valid AddBookDTO addBookDTO, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addBookDTO", addBookDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addBookDTO", bindingResult);
            return new ModelAndView("redirect:/books/add");
        }

        bookService.createBook(addBookDTO);

        return new ModelAndView("redirect:/books/all");
    }


    @GetMapping("/all")
    public ModelAndView allBooks(@PageableDefault(size = 3, sort = "id") Pageable pageable,
                                 @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<AllBooksDTO> allBooksDTO = bookService.findAllBooks(pageable);
        ModelAndView modelAndView = new ModelAndView("all-books");
        modelAndView.addObject("allBooksDTO", allBooksDTO);

        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable("id") Long id) {
        BookDetailsDTO bookDetailsDTO = bookService.findBookById(id);
        ModelAndView modelAndView = new ModelAndView("/book-details");
        modelAndView.addObject("bookDetailsDTO", bookDetailsDTO);
        return modelAndView;
    }

    // Using String and Model here instead of ModelAndView because
    // when im using ModelAndView and im redirected with binding result errors
    // not getting the expected page with error messages but getting the
    // actual page when first go to edit book with fields from book
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        EditBookDTO editBookDTO = bookService.findBookByIdEdit(id);
        if (!model.containsAttribute("editBookDTO")) {
            model.addAttribute("editBookDTO", editBookDTO);
        }
        return "edit-book";
    }

    @PatchMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, @Valid EditBookDTO editBookDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editBookDTO", editBookDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.editBookDTO", bindingResult);
            return new ModelAndView("redirect:/books/edit/" + id);
        }

        Long updatedBookId = bookService.editBook(editBookDTO);

        return new ModelAndView("redirect:/books/details/" + updatedBookId);
    }


    @DeleteMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return new ModelAndView("redirect:/books/all");
    }

    @ModelAttribute("addBookDTO")
    public AddBookDTO addBookDTO() {
        return new AddBookDTO();
    }

    @ModelAttribute("allBooksDTO")
    public AllBooksDTO allBooksDTO() {
        return new AllBooksDTO();
    }



    @ModelAttribute("bookDetailsDTO")
    public BookDetailsDTO bookDetailsDTO() {
        return new BookDetailsDTO();
    }
}
