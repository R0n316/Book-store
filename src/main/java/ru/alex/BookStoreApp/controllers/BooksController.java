package ru.alex.BookStoreApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.models.Person;
import ru.alex.BookStoreApp.models.PersonBook;
import ru.alex.BookStoreApp.services.BookService;
import ru.alex.BookStoreApp.services.ImageService;
import ru.alex.BookStoreApp.services.PersonBookService;
import ru.alex.BookStoreApp.services.PersonDetailsService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final PersonDetailsService personDetailsService;

    private final ImageService imageService;

    private final BookService bookService;

    private final PersonBookService personBookService;


    private static final String IMAGES_DIRECTORY = "src/main/resources/static/images/bookImages";

    @Autowired
    public BooksController(PersonDetailsService personDetailsService, ImageService imageService,
                           BookService bookService, PersonBookService personBookService) {
        this.personDetailsService = personDetailsService;
        this.imageService = imageService;
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
                if(currentBook.isPresent()&&currentBook.get().isFavorite()){
                    book.setFavorite(true);
                }
            });
        }
        model.addAttribute("books",books);
        model.addAttribute("imagesDirectory",IMAGES_DIRECTORY);
        return "books/index";
    }

    @GetMapping("user")
    public String showUserInfo(){
        System.out.println(personDetailsService.getAuthenticatedPerson());
        return "redirect:/books/index";
    }

    @GetMapping("/favorites")
    public String favoritesPage(Model model){
        model.addAttribute(
                "favoriteBooks",
                personBookService.getFavoriteBooksByPerson(personDetailsService.getAuthenticatedPerson())
        );
        return "books/favorites";
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

    @ResponseBody
    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String,String> saveBook(@ModelAttribute Book book,
                                       @RequestParam("adsImages")MultipartFile[] images) throws IOException {
        StringBuilder adsImagesString = new StringBuilder();
        for(MultipartFile imageFile: images){
            adsImagesString.append(imageService.saveImageToStorage(IMAGES_DIRECTORY,imageFile));
        }
        book.setImagePath(adsImagesString.toString());
        bookService.save(book);
        return Map.of("status","OK");
    }


    @ResponseBody
    @PatchMapping("/favoritesByNameAndAuthor")
    public Map<String,String> changeBookFavorites(@RequestParam("personId") int personId,
                                                  @RequestParam("name") String name,
                                                  @RequestParam("author") String author){

        personBookService.addBookToFavorite(personId,name,author);
        return Map.of("status","OK");
    }


    @ResponseBody
    @PatchMapping("/favoritesByBookId")
    public Map<String,String> changeBookFavorites(@RequestParam("personId") int personId,
                                                  @RequestParam("bookId") int bookId){

        personBookService.addBookToFavorite(personId,bookId);
        return Map.of("status","OK");
    }
}
