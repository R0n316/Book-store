package ru.alex.BookStoreApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alex.BookStoreApp.models.Book;
import ru.alex.BookStoreApp.models.Person;
import ru.alex.BookStoreApp.models.PersonBook;
import ru.alex.BookStoreApp.repositories.PersonBookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonBookService {
    private final PersonBookRepository personBookRepository;

    private final PeopleService peopleService;

    private final BookService bookService;
    @Autowired
    public PersonBookService(PersonBookRepository personBookRepository, PeopleService peopleService, BookService bookService) {
        this.personBookRepository = personBookRepository;
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

//    public List<PersonBook> getBooksByPerson(Person person){
//        return personBookRepository.getPersonBooksByPerson(person);
//    }
//
//    public Optional<PersonBook> getPersonBookByBook(Book book){
//        return personBookRepository.getPersonBooksByBook(book);
//    }

    public Optional<PersonBook> findByPersonAndBook(Person person, Book book){
        return personBookRepository.findPersonBookByPersonAndBook(person,book);
    }


    public void addBookToFavorite(int personId,String bookName, String author){
        Person person = peopleService.findByPersonId(personId);
        Book book = bookService.findByNameAndAuthor(bookName,author);
        saveFavoriteBook(person, book);
    }

    public void addBookToFavorite(int personId, int bookId){
        Person person = peopleService.findByPersonId(personId);
        Optional<Book> optionalBook = bookService.findById(bookId);
        assert optionalBook.isPresent();
        Book book = optionalBook.get();
        saveFavoriteBook(person, book);
    }

    private void saveFavoriteBook(Person person, Book book) {
        Optional<PersonBook> personBook = personBookRepository.findPersonBookByPersonAndBook(person,book);
        if(personBook.isPresent()){
            changeFavorites(personBook.get());
            personBookRepository.save(personBook.get());
        }
        else{
            personBookRepository.save(new PersonBook(
                    new PersonBook.PersonBookId(
                            person.getPersonId(),
                            book.getBookId()),
                    person,book,
                    true)
            );
        }
    }

    public void changeFavorites(PersonBook personBook){
        personBook.setFavorite(!personBook.isFavorite());
    }

    public List<Book> getFavoriteBooksByPerson(Person person){
        return personBookRepository.findPersonBookByPerson(person)
                .stream()
                .filter(PersonBook::isFavorite)
                .map(PersonBook::getBook)
                .peek(book -> book.setFavorite(true))
                .toList();
    }
}
