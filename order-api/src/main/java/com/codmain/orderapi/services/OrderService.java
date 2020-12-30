package com.codmain.orderapi.services;

import java.time.LocalDateTime;
import java.util.List;

import com.codmain.orderapi.Exceptions.GeneralServiceException;
import com.codmain.orderapi.Exceptions.NoDataFoundException;
import com.codmain.orderapi.Exceptions.ValidateServiceException;
import com.codmain.orderapi.entity.Order;
import com.codmain.orderapi.entity.OrderLine;
import com.codmain.orderapi.entity.Product;
import com.codmain.orderapi.entity.User;
import com.codmain.orderapi.repository.OrderLineRepository;
import com.codmain.orderapi.repository.OrderRepository;
import com.codmain.orderapi.repository.ProductRepository;
import com.codmain.orderapi.security.UserPrincipal;
import com.codmain.orderapi.validators.OrderValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderLineRepository orderLineRepo;

    @Autowired
    private ProductRepository productRepo;

    public List<Order> findAll(Pageable page) {
        try {
            return orderRepo.findAll(page).toList();
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public Order findById(Long id) {
        try {
            return orderRepo.findById(id).orElseThrow(() -> new NoDataFoundException("La ordern no existe"));
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public void delete(Long id) {
        try {
            Order order = orderRepo.findById(id).orElseThrow(() -> new NoDataFoundException("La orden no existe"));

            orderRepo.delete(order);
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    @Transactional
    public Order create(Order order) {
        try {

            OrderValidator.save(order);

            User user = UserPrincipal.getCurrentUser();

            double total = 0;
            for (OrderLine line : order.getLines()) {
                Product product = productRepo.findById(line.getProduct().getId()).orElseThrow(
                        () -> new NoDataFoundException("no existe el producto " + line.getProduct().getId()));

                product.setName(product.getName());
                line.setPrice(product.getPrice());
                line.setTotal(product.getPrice() * line.getQuantity());
                total += line.getTotal();
            }
            order.setTotal(total);

            order.getLines().forEach(line -> line.setOrder(order));

            if (order.getId() == null) {
                order.setUser(user);
                order.setRegDate(LocalDateTime.now());
                return orderRepo.save(order);
            }

            Order savedOrder = orderRepo.findById(order.getId())
                    .orElseThrow(() -> new NoDataFoundException("La orden no existe"));
            order.setRegDate(savedOrder.getRegDate());

            List<OrderLine> deletedLines = savedOrder.getLines();
            deletedLines.removeAll(order.getLines());
            orderLineRepo.deleteAll(deletedLines);

            return orderRepo.save(order);
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }
}
