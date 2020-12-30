package com.codmain.orderapi.validators;

import com.codmain.orderapi.Exceptions.ValidateServiceException;
import com.codmain.orderapi.entity.Product;

public class ProductValidator {
    public static void save(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ValidateServiceException("El nombre es requerido");
        }

        if (product.getName().length() > 100) {
            throw new ValidateServiceException("El nombre debe terner menos de 100 caracteres");
        }

        if (product.getPrice() == null) {
            throw new ValidateServiceException("El precio es requerido");
        }

        if (product.getPrice() < 0) {
            throw new ValidateServiceException("El precio no puede ser menor a 0");
        }
    }
}
