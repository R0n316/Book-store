package ru.alex.BookStoreApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alex.BookStoreApp.models.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category,String> {

}
