package ua.example.cartify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private int id;

    private int productId;

    private int personId;

    private int quantity;
    
    private int orderId;
}
