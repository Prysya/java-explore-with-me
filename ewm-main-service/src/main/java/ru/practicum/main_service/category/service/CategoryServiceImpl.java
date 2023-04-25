package ru.practicum.main_service.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.dto.NewCategoryDto;
import ru.practicum.main_service.category.exception.CategoryConflictException;
import ru.practicum.main_service.category.mapper.CategoryMapper;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.event.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main_service.category.util.SharedCategoryRequests.checkAndReturnCategory;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getCategories(PageRequest pageRequest) {
        List<Category> categories = categoryRepository.findAll(pageRequest).getContent();

        log.debug("Category Service. Get categories. PageRequest: {}, categories: {}", pageRequest, categories);

        return categories.stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return CategoryMapper.toDto(checkAndReturnCategory(categoryRepository, categoryId));
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.debug("Category Service. Create category by id. NewCategoryDto: {}", newCategoryDto);

        Category category = categoryRepository.save(CategoryMapper.toEntity(newCategoryDto));

        return CategoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        log.debug("Category Service. Delete category by id. CategoryId: {}", categoryId);

        Category category = checkAndReturnCategory(categoryRepository, categoryId);

        if (eventRepository.countByCategoryId(categoryId) > 0L) {
            throw new CategoryConflictException();
        }

        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDto) {
        log.debug("Category Service. Update category. CategoryId: {}, NewCategoryDto: {}", categoryId, newCategoryDto);

        Category category = checkAndReturnCategory(categoryRepository, categoryId);
        category.setName(newCategoryDto.getName());

        return CategoryMapper.toDto(category);
    }
}
