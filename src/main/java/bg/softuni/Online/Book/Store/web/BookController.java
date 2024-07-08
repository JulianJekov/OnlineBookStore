package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.book.AddBookDTO;
import bg.softuni.Online.Book.Store.model.dto.book.AllBooksDTO;
import bg.softuni.Online.Book.Store.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
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
    public ModelAndView allBooks(@PageableDefault(size = 3, sort = "id") Pageable pageable) {
        Page<AllBooksDTO> allBooksDTO = bookService.findAllBooks(pageable);
        ModelAndView modelAndView = new ModelAndView("all-books");
        modelAndView.addObject("allBooksDTO", allBooksDTO);
        return modelAndView;
    }

    }

        modelAndView.addObject("editBookDTO", bookService.findBookByIdEdit(id));
        return modelAndView;
    }

    }

    @ModelAttribute("allBooksDTO")
    public AllBooksDTO allBooksDTO() {
        return new AllBooksDTO();
        return new EditBookDTO();
    }

    @ModelAttribute("bookDetailsDTO")
    public BookDetailsDTO bookDetailsDTO() {
        return new BookDetailsDTO();
    }
}
