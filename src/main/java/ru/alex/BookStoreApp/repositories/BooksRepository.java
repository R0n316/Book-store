package ru.alex.BookStoreApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.models.Category;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book,Integer> {
    Book findByImagePath(String imagePath);

    List<Book> findBooksByNameContainingIgnoreCase(String name);

    List<Book> findBooksByCategory(Category category);
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category where b.category.name = :category AND b.bookLanguage IN (:languages)")
    List<Book> findBooksByBookLanguageInAndCategory(List<String> languages, Category category);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category where b.bookLanguage IN (:languages)")
    List<Book> findAllByBookLanguageIn(List<String> languages);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category")
    List<Book> getAllBooks();
}
