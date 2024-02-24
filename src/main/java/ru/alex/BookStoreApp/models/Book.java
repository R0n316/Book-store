package ru.alex.BookStoreApp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Column(name = "book_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "series")
    private String series;

    @Column(name = "year_of_publishing")
    private int yearOfPublishing;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "number_of_pages")
    private int numberOfPages;

    @Column(name = "size")
    private String size;

    @Column(name = "cover_type")
    private String coverType;

    @Column(name = "circulation")
    private int circulation;

    @Column(name = "weight")
    private int weight;

    @Column(name = "age_restrictions")
    private int ageRestrictions;

    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "price")
    private int price;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @Column(name = "is_in_cart")
    private boolean isInCart;

    @Column(name = "description")
    private String description;


    @OneToMany(mappedBy = "book")
    private List<PersonBook> personBooks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId && yearOfPublishing == book.yearOfPublishing && numberOfPages == book.numberOfPages && circulation == book.circulation && weight == book.weight && ageRestrictions == book.ageRestrictions && price == book.price && Objects.equals(name, book.name) && Objects.equals(author, book.author) && Objects.equals(publisher, book.publisher) && Objects.equals(series, book.series) && Objects.equals(isbn, book.isbn) && Objects.equals(size, book.size) && Objects.equals(coverType, book.coverType) && Objects.equals(rating, book.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, name, author, publisher, series, yearOfPublishing, isbn, numberOfPages, size, coverType, circulation, weight, ageRestrictions, rating, price);
    }
}
