package ua.example.cartify.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.model.Product;
import ua.example.cartify.service.ProductService;

@Component
@RequiredArgsConstructor
public class ProductValidator implements Validator {
    private final ProductService productService;
    
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.equals(clazz);  
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;

        if (productService.getByArticle(product.getArticle()).isPresent()) {
            errors.rejectValue("article", "", "This article is already used");
        }
    }

}
