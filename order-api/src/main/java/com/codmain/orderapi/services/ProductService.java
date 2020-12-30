package com.codmain.orderapi.services;

import com.codmain.orderapi.Exceptions.GeneralServiceException;
import com.codmain.orderapi.Exceptions.NoDataFoundException;
import com.codmain.orderapi.Exceptions.ValidateServiceException;
import com.codmain.orderapi.entity.Product;
import com.codmain.orderapi.repository.ProductRepository;
import com.codmain.orderapi.validators.ProductValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

// esto le dice a springboot que cuando inicie la aplicacion crea una instancia de este objeto y luego lo puedo inyectar yo en otros objetos con autowired
@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo; // el producto debe proporcionar todos los metodos de negocio que necesite el
                                           // controller

    // creamos una variable de tipo repositorio
    // a esta variable hay que inyectarle una instancia de ProductRepository, para
    // eso es @Autowired, le decimos que la instancia que ya creo en
    // ProductRepository.java la inyecte en la variable productRepo, ya no hace
    // falta incializar con un new

    @Transactional
    public List<Product> findAll(Pageable page) {
        try {
            List<Product> products = productRepo.findAll(page).toList();
            return products;
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }

    }

    @Transactional
    public Product findById(Long productId) {
        try {
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new NoDataFoundException("No existe el producto"));
            return product;
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    @Transactional
    public Product create(Product product) {

        try {
            ProductValidator.save(product);

            if (product.getId() == null) {
                // no uso else porque ya estoy retornando aca
                Product newProduct = productRepo.save(product);
                return newProduct;
            }

            Product exitProduct = productRepo.findById(product.getId())
                    .orElseThrow(() -> new NoDataFoundException("No existe el producto"));

            exitProduct.setName(product.getName());
            exitProduct.setPrice(product.getPrice());

            productRepo.save(exitProduct);

            return exitProduct;
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }

    }

    @Transactional
    public void delete(Long productId) {
        try {
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new NoDataFoundException("No existe el producto"));
            productRepo.delete(product);
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

}
