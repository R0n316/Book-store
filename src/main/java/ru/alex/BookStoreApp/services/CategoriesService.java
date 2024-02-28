package ru.alex.BookStoreApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alex.BookStoreApp.models.Category;
import ru.alex.BookStoreApp.repositories.CategoriesRepository;

import java.util.List;

@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Category> findAll(){
        return categoriesRepository.findAll();
    }
}
