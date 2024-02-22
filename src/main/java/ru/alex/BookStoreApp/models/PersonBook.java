package ru.alex.BookStoreApp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "person_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonBook {

    @EmbeddedId
    private PersonBookId id;

    @ManyToOne
    @MapsId("personId")
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "is_favorite")
    private boolean isFavorite;



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class PersonBookId implements Serializable{
        @Column(name = "person_id", insertable = false, updatable = false)
        private int personId;

        @Column(name = "book_id",insertable = false, updatable = false)
        private int bookId;

    }
}

