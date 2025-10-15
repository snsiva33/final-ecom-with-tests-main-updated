package com.example.ecommerce.service;

import com.example.ecommerce.entity.OrderEntity;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;

    public OrderService(OrderRepository orderRepo, ProductRepository productRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    public OrderEntity createOrder(OrderEntity order) {
        Product p = productRepo.findById(order.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (p.getStock() < order.getQuantity()) {
            throw new BadRequestException("Insufficient stock");
        }
        double total = p.getPrice() * order.getQuantity();
        order.setTotalPrice(total);
        order.setStatus("CREATED");
        OrderEntity saved = orderRepo.save(order);

        // reduce stock
        p.setStock(p.getStock() - order.getQuantity());
        productRepo.save(p);

        return saved;
    }

    public OrderEntity getById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<OrderEntity> getAll() {
        return orderRepo.findAll();
    }

    public void delete(Long id) {
        OrderEntity o = getById(id);
        orderRepo.delete(o);
    }
}
