package ru.alex.BookStoreApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.models.Category;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book,Integer> {
    Book findByImagePath(String imagePath);

    List<Book> findBooksByNameContaining(String name);

    List<Book> findBooksByCategory(Category category);

    List<Book> findBooksByBookLanguageInAndCategory(List<String> languages, Category category);

    List<Book> findAllByBookLanguageIn(List<String> languages);
}
