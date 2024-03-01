package ru.alex.BookStoreApp.controllers;

import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.services.BookService;
import ru.alex.BookStoreApp.services.ImageService;
import ru.alex.BookStoreApp.services.PersonBookService;
import ru.alex.BookStoreApp.util.BookErrorResponse;
import ru.alex.BookStoreApp.util.BookNotFoundException;

import java.io.IOException;
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
    public ResponseEntity<HttpStatus> saveBook(@ModelAttribute Book book,
                                       @RequestParam("adsImages") MultipartFile[] images) throws IOException {
        StringBuilder adsImagesString = new StringBuilder();
        for(MultipartFile imageFile: images){
            adsImagesString.append(imageService.saveImageToStorage(IMAGES_DIRECTORY,imageFile));
        }
        book.setImagePath(adsImagesString.toString());
        bookService.save(book);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PatchMapping("/toFavorites")
    public ResponseEntity<HttpStatus> changeBookFavorites(@RequestParam("personId") int personId,
                                                  @RequestParam("imagePath") String imagePath){

        personBookService.saveBookToFavorite(personId,bookService.findByImagePath(imagePath));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/toFavoritesById")
    public ResponseEntity<HttpStatus> changeBookFavorites(@RequestParam("personId") int personId,
                                                  @RequestParam("bookId") int bookId){
        Optional<Book> book = bookService.findById(bookId);
        if(book.isEmpty()){
            throw new BookNotFoundException();
        }
        personBookService.saveBookToFavorite(personId,book.get());
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PatchMapping("/toCart")
    public ResponseEntity<HttpStatus> changeBookInCart(@RequestParam("personId") int personId,
                                                       @RequestParam("imagePath") String imagePath){
        personBookService.saveBookToCart(personId,bookService.findByImagePath(imagePath));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<BookErrorResponse> handleException(BookNotFoundException e){
        BookErrorResponse response = new BookErrorResponse(
                "книга не найдена",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

}
