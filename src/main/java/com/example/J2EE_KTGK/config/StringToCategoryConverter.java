package com.example.J2EE_KTGK.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.J2EE_KTGK.entity.Category;
import com.example.J2EE_KTGK.repository.CategoryRepository;

@Component
public class StringToCategoryConverter implements Converter<String, Category> {

    private final CategoryRepository categoryRepository;

    public StringToCategoryConverter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }

        Long categoryId = Long.valueOf(source);
        return categoryRepository.findById(categoryId).orElse(null);
    }
}
