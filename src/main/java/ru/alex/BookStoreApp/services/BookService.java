package ru.alex.BookStoreApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.models.Category;
import ru.alex.BookStoreApp.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BooksRepository booksRepository;

    @Autowired
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public void save(Book book){
        booksRepository.save(book);
    }


    public List<Book> findAll(){
        return booksRepository.getAllBooks();
    }


    public Optional<Book> findById(int bookId){
        return booksRepository.findById(bookId);
    }
    public Book findByImagePath(String imagePath){
        return booksRepository.findByImagePath(imagePath);
    }

    public List<Book> findBooksContainingName(String name){
        return booksRepository.findBooksByNameContainingIgnoreCase(name);
    }

    public List<Book> getHighRatingBooks(){
        return findAll().stream()
                .sorted(((o1, o2) -> o2.getRating().compareTo(o1.getRating()))).toList().subList(0,4);
    }

    public List<Book> getPopularBooks(){
        return findAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getCirculation(),o1.getCirculation())).toList().subList(0,4);
    }

    public List<Book> findBooksByCategory(String category){
        if(category.equals("Все")){
            return findAll();
        }
        return booksRepository.findBooksByCategory(new Category(category));
    }

    public List<Book> findAllByLanguages(List<String> languages){
        return booksRepository.findAllByBookLanguageIn(languages);
    }
    public List<Book> findBookByLanguagesAndCategory(List<String> languages, String category){
        if(category.equals("Все")){
            return findAllByLanguages(languages);
        }
        return booksRepository.findBooksByBookLanguageInAndCategory(languages, new Category(category));
    }
}
