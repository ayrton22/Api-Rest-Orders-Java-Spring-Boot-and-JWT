package com.codmain.orderapi.validators;

import com.codmain.orderapi.Exceptions.ValidateServiceException;
import com.codmain.orderapi.entity.Order;
import com.codmain.orderapi.entity.OrderLine;

public class OrderValidator {
    public static void save(Order order) {
        if (order.getLines() == null || order.getLines().isEmpty()) {
            throw new ValidateServiceException("Las lineas son requeridas");
        }

        for (OrderLine line : order.getLines()) {
            if (line.getProduct() == null || line.getProduct().getId() == null) {
                throw new ValidateServiceException("El producto es requerido");
            }
            if (line.getQuantity() == null) {
                throw new ValidateServiceException("La cantidad es requerida");
            }
            if (line.getQuantity() < 0) {
                throw new ValidateServiceException("La cantidad es incorrecta");
            }
        }
    }
}
