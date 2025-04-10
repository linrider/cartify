package ua.example.cartify.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Category;
import ua.example.cartify.service.CategoryService;

@Component
@RequiredArgsConstructor
public class CategoryValidator implements Validator {

    private final CategoryService categoryService;
    
    @Override
    public boolean supports(Class<?> clazz) {
        return Category.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Category category = (Category) target;

        if (categoryService.getByName(category.getName()).isPresent()) {
            errors.rejectValue("name", "", "This category name is already used");
        }
    }

}
