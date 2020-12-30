package com.codmain.orderapi.repository;

import com.codmain.orderapi.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//le decimos a springboot, que cuando incie la aplicación creee una instancia de este objeto y la mantenga en memoria, en springframework es llamado bin, este bean va a estar gestionado por spring
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
