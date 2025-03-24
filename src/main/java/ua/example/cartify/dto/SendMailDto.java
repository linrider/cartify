package ua.example.cartify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMailDto {
    private String to;
    private String subject;
    private String text;
}
