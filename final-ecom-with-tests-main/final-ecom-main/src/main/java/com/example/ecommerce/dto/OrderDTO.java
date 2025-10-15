package com.example.ecommerce.dto;

public record OrderDTO(Long id, Long userId, Long productId, Integer quantity, Double totalPrice, String status) {}
