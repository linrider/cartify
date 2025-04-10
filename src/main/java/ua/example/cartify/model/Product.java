package ua.example.cartify.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;

    @NotBlank(message = "The username could not be empty")
    @Size(min = 3, max = 255, message = "The username length should be from 3 to 255")
    private String name;

    @Size(min = 3, max = 1000, message = "The username length should be from 3 to 30")
    private String description;

    @NotBlank(message = "The username could not be empty")
    @Size(min = 3, max = 50, message = "The username length should be from 3 to 30")
    private String article;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 10 digits with 2 decimal places")
    private float price;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0)
    private int quantity;

    private int categoryId;

    private LocalDateTime createdAt = LocalDateTime.now();

}
