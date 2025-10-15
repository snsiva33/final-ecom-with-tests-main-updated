package com.example.ecommerce.integration;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EcommerceIntegrationTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void test_save_and_find() {
        Product p = Product.builder().name("IntegrationProduct").description("desc").price(9.99).stock(20).category("Test").build();
        Product saved = productRepository.save(p);
        assertNotNull(saved.getId());

        List<Product> found = productRepository.findByNameContainingIgnoreCase("Integration");
        assertFalse(found.isEmpty());
    }
}
