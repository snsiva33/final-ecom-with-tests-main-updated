package com.example.ecommerce.dto;

public record ProductDTO(Long id, String name, String description, Double price, Integer stock, String category) {}
