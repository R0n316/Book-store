package ru.alex.BookStoreApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.models.Person;
import ru.alex.BookStoreApp.models.PersonBook;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonBookRepository extends JpaRepository<PersonBook, PersonBook.PersonBookId> {


    Optional<PersonBook> findPersonBookByPersonAndBook(Person person, Book book);

    List<PersonBook> findByPerson(Person person);

    @Query("SELECT pb FROM PersonBook pb JOIN FETCH pb.book LEFT JOIN FETCH pb.book.category WHERE pb.person = :person AND pb.isInCart = true")
    List<PersonBook> findByPersonWithBooksInCart(Person person);

    @Query("SELECT pb FROM PersonBook pb JOIN FETCH pb.book LEFT JOIN FETCH pb.book.category WHERE pb.person = :person AND pb.isFavorite = true")
    List<PersonBook> findByPersonWithBooksInFavorites(Person person);
}
