package ru.practicum.main_service.category.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.main_service.category.exception.CategoryNotFoundException;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;

@Slf4j
@UtilityClass
public class SharedCategoryRequests {
    public static Category checkAndReturnCategory(CategoryRepository categoryRepository, Long categoryId) {
        log.debug("SharedCategoryRequests check category. Category id: {}", categoryId);

        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}
