package ua.example.cartify.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private int id;

    @NotBlank(message = "The username could not be empty")
    @Size(min = 3, max = 30, message = "The username length should be from 3 to 50")
    private String name;

    private String description;
}
