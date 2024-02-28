package ru.alex.BookStoreApp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Book> books;

    public Category(String name) {
        this.name = name;
    }
}
