package ru.alex.BookStoreApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.models.Person;
import ru.alex.BookStoreApp.models.PersonBook;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonBookRepository extends JpaRepository<PersonBook, PersonBook.PersonBookId> {
//    @Query("""
//            SELECT pb
//            FROM PersonBook pb
//            WHERE pb.person.personId = :personId
//""")
//    List<Book> getBooksByPerson(int personId);

//    List<PersonBook> getPersonBooksByPerson(Person person);
//
//    Optional<PersonBook> getPersonBooksByBook(Book book);


    Optional<PersonBook> findPersonBookByPersonAndBook(Person person, Book book);
    List<PersonBook> findPersonBookByPerson(Person person);
}
