package ru.alex.BookStoreApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.models.Person;
import ru.alex.BookStoreApp.models.PersonBook;
import ru.alex.BookStoreApp.services.BookService;
import ru.alex.BookStoreApp.services.PersonBookService;
import ru.alex.BookStoreApp.services.PersonDetailsService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final PersonDetailsService personDetailsService;



    private final BookService bookService;

    private final PersonBookService personBookService;


    @Autowired
    public BooksController(PersonDetailsService personDetailsService, BookService bookService,
                           PersonBookService personBookService) {
        this.personDetailsService = personDetailsService;
        this.bookService = bookService;
        this.personBookService = personBookService;
    }

    @GetMapping
    public String index(Model model){
        Person authenticatedPerson = personDetailsService.getAuthenticatedPerson();
        model.addAttribute("person",authenticatedPerson);
        List<Book> books = bookService.getBooks();
        books.sort(Comparator.comparing(Book::getName));
        if(authenticatedPerson!=null){
            books.forEach(book -> {
                Optional<PersonBook> currentBook = personBookService.findByPersonAndBook(authenticatedPerson,book);
                if(currentBook.isPresent()){
                    if(currentBook.get().isFavorite()){
                        book.setFavorite(true);
                    }
                    if(currentBook.get().isInCart()){
                        book.setInCart(true);
                    }
                }
            });
        }
        model.addAttribute("books",books);
        return "books/index";
    }

    @GetMapping("/favorites")
    public String favoritesPage(Model model){
        model.addAttribute(
                "favoriteBooks",
                personBookService.getFavoriteBooksByPerson(personDetailsService.getAuthenticatedPerson())
        );
        return "books/favorites";
    }

    @GetMapping("/inCart")
    public String bookInCartPage(Model model){
        List<Book> books = personBookService.getInCartBooksByPerson(personDetailsService.getAuthenticatedPerson());
        model.addAttribute(
                "inCartBooks",
                personBookService.getInCartBooksByPerson(personDetailsService.getAuthenticatedPerson())
        );
        return "books/cart";
    }

    @GetMapping("/{bookId}")
    public String bookInfo(@PathVariable int bookId, Model model){
        Optional<Book> optionalBook = bookService.findById(bookId);
        Person authenticatedPerson = personDetailsService.getAuthenticatedPerson();
        if(optionalBook.isEmpty()){
            return "redirect:/books";
        }
        Book book = optionalBook.get();
        if(authenticatedPerson!=null){
            Optional<PersonBook> currentBook = personBookService.findByPersonAndBook(authenticatedPerson,book);
            if(currentBook.isPresent()&&currentBook.get().isFavorite()){
                book.setFavorite(true);
            }
        }
        model.addAttribute("book",book);
        model.addAttribute("person",authenticatedPerson);
        return "books/bookInfo";
    }
}
