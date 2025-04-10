package ua.example.cartify.service;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.example.cartify.exceptions.ProductOutOfStockException;
import ua.example.cartify.model.CartItem;
import ua.example.cartify.model.Product;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final JdbcTemplate jdbcTemplate;
    private final ProductService productService;

    public void save(int personId, int productId) {
        Product product = productService.getById(productId);
        if (product.getQuantity() < 1) {
            throw new ProductOutOfStockException(product.getName() + " out of stock");
        }

        CartItem cartItem = new CartItem();
        cartItem.setPersonId(personId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(1);
    }

    public CartItem getById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM cart_item WHERE id=?",
                new BeanPropertyRowMapper<>(CartItem.class),
                id);
    }

    public void deleteById(int id) {
        jdbcTemplate
                .update("DELETE FROM cart_item WHERE id=?", id);
    }

    public void increaseQuntity(int cartItemId) {
        CartItem cartItem = getById(cartItemId);
        Product product = productService.getById(cartItem.getProductId());
        if (cartItem.getQuantity() + 1 > product.getQuantity()) {
            throw new ProductOutOfStockException(product.getName() + " out of stock");
        }

        jdbcTemplate.update("UPDATE cart_item SET qauntity=quantity+1 WHERE id=?", cartItemId); 
    }

    public void decreaseQuantity(int cartItemId) {
        CartItem cartItem = getById(cartItemId);
        if (cartItem.getQuantity() == 0) {
            throw new ProductOutOfStockException("The cart is empty");
        }

        jdbcTemplate.update("UPDATE cart_item SET quantity=quantity-1 where id=?", cartItem);
    }
}

