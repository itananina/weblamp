package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.converters.DtoConverter;
import com.itananina.weblamp.weblamp.dto.ProductDto;
import com.itananina.weblamp.weblamp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;
    private final DtoConverter productConverter;

    @GetMapping
    public Page<ProductDto> showProducts(
        @RequestParam(defaultValue = "1") Integer page, //не указываю name тк совпадает
        @RequestParam(name="min_price", required = false) Integer minPrice,
        @RequestParam(name="max_price", required = false) Integer maxPrice,
        @RequestParam(name="title_part", required = false) String titlePart
    ){
        if (page < 1) {
            page = 1;
        }
        return productService.find(page, minPrice, maxPrice, titlePart)
                .map(p->productConverter.productToProductDto(p));
    }

//    @GetMapping("{id}")
//    public ProductDto showProduct(@PathVariable Long id) {
//        return productConverter.productToProductDto(productService.findById(id));
//    }


}
