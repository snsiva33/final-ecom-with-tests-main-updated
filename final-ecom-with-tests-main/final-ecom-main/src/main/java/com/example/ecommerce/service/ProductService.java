package com.example.ecommerce.service;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo) { this.repo = repo; }

    public Product create(Product p) { return repo.save(p); }

    public Product getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public List<Product> getAll() { return repo.findAll(); }

    public Product update(Long id, Product p) {
        Product existing = getById(id);
        existing.setName(p.getName());
        existing.setDescription(p.getDescription());
        existing.setPrice(p.getPrice());
        existing.setStock(p.getStock());
        existing.setCategory(p.getCategory());
        return repo.save(existing);
    }

    public void delete(Long id) {
        Product p = getById(id);
        repo.delete(p);
    }

    public List<Product> search(String name, String category) {
        if (name != null && category != null) {
            return repo.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(name, category);
        } else if (name != null) {
            return repo.findByNameContainingIgnoreCase(name);
        } else if (category != null) {
            return repo.findByCategoryIgnoreCase(category);
        } else {
            return repo.findAll();
        }
    }

    public void reduceStock(Long productId, int quantity) {
        Product p = getById(productId);
        if (p.getStock() == null || p.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        p.setStock(p.getStock() - quantity);
        repo.save(p);
    }
}
