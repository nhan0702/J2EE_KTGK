package com.example.J2EE_KTGK.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.J2EE_KTGK.entity.Category;
import com.example.J2EE_KTGK.repository.CategoryRepository;

@Component
public class CategoryConverter implements Converter<String, Category> {

    private final CategoryRepository categoryRepository;

    public CategoryConverter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        try {
            Long id = Long.parseLong(source.trim());
            return categoryRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
