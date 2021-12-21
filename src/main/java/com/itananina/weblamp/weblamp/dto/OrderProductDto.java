package com.itananina.weblamp.weblamp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private Long id;
    private String title;
    private int price;
    private int amount;
}
