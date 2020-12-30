package com.codmain.orderapi.controllers;

//import java.util.logging.Logger; // logger a mano

import com.codmain.orderapi.entity.Product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j // logger

// este componente debe ser tomando como un bin y puesto en el contexto de todos
// los objetos que van a estar disponibles en Spring y le decimos que va a haber
// servicios REST
@RestController
public class HelloWorldController {

    // logger a mano

    // private static final Logger log =
    // Logger.getLogger(HelloWorldController.class.getCanonicalName());

    @GetMapping(value = "hello")

    public String hello() {
        return "Hello World";
    }
}
