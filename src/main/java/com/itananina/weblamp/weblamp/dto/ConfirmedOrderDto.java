package com.itananina.weblamp.weblamp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedOrderDto {
    private Long id;
    private String status;
    private int total;
    private int count;
}
