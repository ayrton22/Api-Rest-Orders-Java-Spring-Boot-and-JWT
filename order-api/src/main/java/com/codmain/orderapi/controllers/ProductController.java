package com.codmain.orderapi.controllers;

import java.util.ArrayList;
import java.util.List;

import com.codmain.orderapi.converters.ProductConverter;
import com.codmain.orderapi.dtos.ProductDTO;
import com.codmain.orderapi.entity.Product;
import com.codmain.orderapi.services.ProductService;
import com.codmain.orderapi.utils.WrapperResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    // creamos una variable de tipo repositorio
    // a esta variable hay que inyectarle una instancia de ProductRepository, para
    // eso es @Autowired, le decimos que la instancia que ya creo en
    // ProductRepository.java la inyecte en la variable productRepo, ya no hace
    // falta incializar con un new
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductConverter converter;

    private List<Product> products = new ArrayList<>();

    public ProductController() {

        for (int c = 0; c < 10; c++) {
            products.add(Product.builder().id(c + 1L).name("Producto " + (c + 1L)).build());
        }

    }

    @GetMapping(value = "/products")
    public ResponseEntity<List<ProductDTO>> findAll(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) {

        Pageable page = PageRequest.of(pageNumber, pageSize);

        List<Product> products = productService.findAll(page);

        List<ProductDTO> dtoProducts = converter.fromEntity(products);

        return new WrapperResponse(true, "success", dtoProducts).createResponse(HttpStatus.OK);
    }

    @GetMapping(value = "/products/{productId}")
    public ResponseEntity<WrapperResponse<ProductDTO>> findById(@PathVariable("productId") Long productId) {
        Product product = productService.findById(productId);

        ProductDTO productDTO = converter.fromEntity(product);

        return new WrapperResponse<ProductDTO>(true, "succes", productDTO).createResponse(HttpStatus.OK);
    }

    @PostMapping(value = "/products")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product) {
        Product newProduct = productService.create(converter.fromDTO(product));

        ProductDTO productDTO = converter.fromEntity(newProduct);

        return new WrapperResponse(true, "success", productDTO).createResponse(HttpStatus.CREATED);
    }

    @PutMapping(value = "/products")
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO product) {
        Product updateProduct = productService.create(converter.fromDTO(product));

        ProductDTO productDTO = converter.fromEntity(updateProduct);

        return new WrapperResponse(true, "success", productDTO).createResponse(HttpStatus.OK);
    }

    @DeleteMapping(value = "/products/{productId}")
    public ResponseEntity<?> delete(@PathVariable("productId") Long productId) {
        productService.delete(productId);

        return new WrapperResponse<>(true, "success", null).createResponse(HttpStatus.OK);
    }
}
