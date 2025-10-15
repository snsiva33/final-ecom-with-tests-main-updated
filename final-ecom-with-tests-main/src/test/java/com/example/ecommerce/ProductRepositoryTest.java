package com.example.ecommerce;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFind() {
        Product p = new Product();
        p.setName("RepoProduct");
        p.setDescription("repo desc");
        p.setPrice(50.0);
        p.setStock(3);

        Product saved = productRepository.save(p);
        assertThat(saved.getId()).isNotNull();

        var found = productRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("RepoProduct");
    }
}
