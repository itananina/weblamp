package com.itananina.weblamp.weblamp.dto;

import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String status;
    private int total;
    private List<OrderProductDto> productList;
    private int count;
}
