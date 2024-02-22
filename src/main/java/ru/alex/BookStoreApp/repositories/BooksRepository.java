package ru.alex.BookStoreApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alex.BookStoreApp.models.Book;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book,Integer> {
    Book findByNameAndAuthor (String name, String author);
}
