package ru.alex.BookStoreApp.controllers;

import jakarta.annotation.security.PermitAll;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.services.BookService;
import ru.alex.BookStoreApp.services.ImageService;
import ru.alex.BookStoreApp.services.PersonBookService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/books/api")
@PermitAll
public class BooksApiController {

    private final ImageService imageService;

    private final BookService bookService;

    private final PersonBookService personBookService;
    private static final String IMAGES_DIRECTORY = "src/main/resources/static/images/bookImages";

    public BooksApiController(ImageService imageService, BookService bookService, PersonBookService personBookService) {
        this.imageService = imageService;
        this.bookService = bookService;
        this.personBookService = personBookService;
    }

    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String,String> saveBook(@ModelAttribute Book book,
                                       @RequestParam("adsImages") MultipartFile[] images) throws IOException {
        StringBuilder adsImagesString = new StringBuilder();
        for(MultipartFile imageFile: images){
            adsImagesString.append(imageService.saveImageToStorage(IMAGES_DIRECTORY,imageFile));
        }
        book.setImagePath(adsImagesString.toString());
        bookService.save(book);
        return Map.of("status","OK");
    }


    @PatchMapping("/toFavorites")
    public Map<String,String> changeBookFavorites(@RequestParam("personId") int personId,
                                                  @RequestParam("imagePath") String imagePath){

        personBookService.saveBookToFavorite(personId,bookService.findByImagePath(imagePath));
        return Map.of("status","OK");
    }

    @PatchMapping("/toFavoritesById")
    public Map<String,String> changeBookFavorites(@RequestParam("personId") int personId,
                                                  @RequestParam("bookId") int bookId){
        Optional<Book> book = bookService.findById(bookId);
        if(book.isEmpty()){
            return Map.of("error","book is empty");
        }
        personBookService.saveBookToFavorite(personId,book.get());
        return Map.of("status","OK");
    }

    // TODO исправить возврат реузльтата в REST методах на ResponseEntity

    @PatchMapping("/toCart")
    public Map<String,String> changeBookInCart(@RequestParam("personId") int personId,
                                               @RequestParam("imagePath") String imagePath){
        personBookService.saveBookToCart(personId,bookService.findByImagePath(imagePath));
        return Map.of("status","OK");
    }

    @PatchMapping("/toCartById")
    public Map<String,String> changeBookInCart(@RequestParam("personId") int personId,
                                               @RequestParam("bookId") int bookId){
        Optional<Book> book = bookService.findById(bookId);
        if(book.isEmpty()){
            return Map.of("error","book is empty");
        }
        personBookService.saveBookToCart(personId,book.get());
        return Map.of("status","OK");
    }


    // TODO посмотреть почему при попытке доавбить книгу в корзину ничего не происходит
}
