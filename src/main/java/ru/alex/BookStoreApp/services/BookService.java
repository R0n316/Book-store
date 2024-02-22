package ru.alex.BookStoreApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alex.BookStoreApp.models.Book;
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

    public List<Book> getBooks(){
        return booksRepository.findAll();
    }

    public void changeFavorites(Book book){
        book.setFavorite(!book.isFavorite());
        booksRepository.save(book);
    }

    public Book findByNameAndAuthor(String name, String author){
        return booksRepository.findByNameAndAuthor(name,author);
    }

    public Optional<Book> findById(int bookId){
        return booksRepository.findById(bookId);
    }
}
