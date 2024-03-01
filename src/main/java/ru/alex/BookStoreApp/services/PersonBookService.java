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

    @Autowired
    public PersonBookService(PersonBookRepository personBookRepository, PeopleService peopleService) {
        this.personBookRepository = personBookRepository;
        this.peopleService = peopleService;
    }

    public Optional<PersonBook> findByPersonAndBook(Person person, Book book){
        return personBookRepository.findPersonBookByPersonAndBook(person,book);
    }

    public List<PersonBook> findByPerson(Person person){
        return personBookRepository.findByPerson(person);
    }

    public void saveBookToFavorite(int personId, Book book) {
        Person person = peopleService.findByPersonId(personId);
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
                    true,false)
            );
        }
    }

    public void saveBookToCart(int personId, Book book) {
        Person person = peopleService.findByPersonId(personId);
        Optional<PersonBook> personBook = personBookRepository.findPersonBookByPersonAndBook(person,book);
        if(personBook.isPresent()){
            changeToCart(personBook.get());
            personBookRepository.save(personBook.get());
        }
        else{
            personBookRepository.save(new PersonBook(
                    new PersonBook.PersonBookId(
                            person.getPersonId(),
                            book.getBookId()),
                    person,book,
                    false,true)
            );
        }
    }

    public void changeFavorites(PersonBook personBook){
        personBook.setFavorite(!personBook.isFavorite());
    }

    public void changeToCart(PersonBook personBook){
        personBook.setInCart(!personBook.isInCart());
    }

    public List<Book> getFavoriteBooksByPerson(Person person){
        return personBookRepository.findByPersonWithBooksInFavorites(person)
                .stream()
                .map(PersonBook::getBook)
                .peek(book -> book.setFavorite(true))
                .toList();
    }

    public List<Book> getInCartBooksByPerson(Person person){
        return personBookRepository.findByPersonWithBooksInCart(person)
                .stream()
                .map(PersonBook::getBook)
                .peek(book -> book.setInCart(true))
                .toList();
    }
}
