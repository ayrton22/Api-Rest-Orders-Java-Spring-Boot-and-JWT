package com.codmain.orderapi.validators;

import com.codmain.orderapi.Exceptions.ValidateServiceException;
import com.codmain.orderapi.entity.User;

public class UserValidator {
    public static void signup(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new ValidateServiceException("El nombre de usuario es requerido");
        }

        if (user.getUsername().length() > 30) {
            throw new ValidateServiceException("el nombre de usuario no puede ser mayor a 30 caracteres");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new ValidateServiceException("La password es requerida");
        }

        if (user.getPassword().length() > 30) {
            throw new ValidateServiceException("La password no puede tener mas de 30 caracteres");
        }
    }
}
