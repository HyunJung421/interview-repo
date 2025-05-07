package com.portfolio.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.interview.entity.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Categories findByName(String category);
    
}
