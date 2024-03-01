package ru.alex.BookStoreApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.models.Person;
import ru.alex.BookStoreApp.models.PersonBook;
import ru.alex.BookStoreApp.services.BookService;
import ru.alex.BookStoreApp.services.CategoriesService;
import ru.alex.BookStoreApp.services.PersonBookService;
import ru.alex.BookStoreApp.services.PersonDetailsService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final PersonDetailsService personDetailsService;

    private final BookService bookService;

    private final PersonBookService personBookService;

    private final CategoriesService categoriesService;

    @Autowired
    public BooksController(PersonDetailsService personDetailsService, BookService bookService,
                           PersonBookService personBookService, CategoriesService categoriesService) {
        this.personDetailsService = personDetailsService;
        this.bookService = bookService;
        this.personBookService = personBookService;
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public String index(Model model){
        Person authenticatedPerson = personDetailsService.getAuthenticatedPerson();
        model.addAttribute("person",authenticatedPerson);
        List<Book> highRatingBooks = new ArrayList<>(bookService.getHighRatingBooks());
        List<Book> popularBooks = new ArrayList<>(bookService.getPopularBooks());
        highRatingBooks.sort(Comparator.comparing(Book::getName));
        popularBooks.sort(Comparator.comparing(Book::getName));
        setBooksFavoriteAndInCart(highRatingBooks,authenticatedPerson);
        setBooksFavoriteAndInCart(popularBooks,authenticatedPerson);
        model.addAttribute("highRatingBooks",highRatingBooks);
        model.addAttribute("popularBooks",popularBooks);
        model.addAttribute("categories",categoriesService.findAll());
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
        model.addAttribute(
                "inCartBooks",
                personBookService.getInCartBooksByPerson(personDetailsService.getAuthenticatedPerson())
        );
        return "books/cart";
    }

    @GetMapping("/found-books")
    public String findBookLikeName(@RequestParam("name") String name, Model model){
        List<Book> books = bookService.findBooksContainingName(name);
        Person authenticatedPerson = personDetailsService.getAuthenticatedPerson();
        setBooksFavoriteAndInCart(books,authenticatedPerson);
        model.addAttribute("books",bookService.findBooksContainingName(name));
        model.addAttribute("person", authenticatedPerson);
        return "books/foundBooks";
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

    @GetMapping("/category")
    public String booksByCategory(@RequestParam("category") String category, Model model){
        Person authenticatedPerson = personDetailsService.getAuthenticatedPerson();
        List<Book> books = bookService.findBooksByCategory(category);
        if(authenticatedPerson!=null){
            setBooksFavoriteAndInCart(books,authenticatedPerson);
        }
        model.addAttribute("books",books);
        model.addAttribute("category",category);
        model.addAttribute("person", authenticatedPerson);
        model.addAttribute("categories",categoriesService.findAll());
        return "books/category";
    }

    @GetMapping("/filter")
    public String filterBooks(
            @RequestParam(name = "language", required = false) String languagesStr,
            @RequestParam(name = "category", required = false) String category, Model model) {
        List<String> languages;
        if (StringUtils.hasText(languagesStr)) {
            languages = Arrays.asList(languagesStr.split(","));
            model.addAttribute("books",bookService.findBookByLanguagesAndCategory(languages,category));
        }
        else{
            model.addAttribute("books",bookService.findBooksByCategory(category));
        }
        return "books/bookList";
    }

    private void setBooksFavoriteAndInCart(List<Book> books, Person authenticatedPerson) {
        if (authenticatedPerson != null) {
            List<PersonBook> personBooks = personBookService.findByPerson(authenticatedPerson);

            Map<Integer, PersonBook> personBookMap = personBooks.stream()
                    .collect(Collectors.toMap(pb -> pb.getBook().getBookId(), pb -> pb));

            books.forEach(book -> {
                PersonBook currentBook = personBookMap.get(book.getBookId());
                if (currentBook != null) {
                    book.setFavorite(currentBook.isFavorite());
                    book.setInCart(currentBook.isInCart());
                }
            });
        }
    }
}