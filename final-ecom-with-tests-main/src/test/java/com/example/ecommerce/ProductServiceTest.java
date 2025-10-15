package com.example.ecommerce;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void whenFindById_thenReturnProduct() {
        Product p = new Product();
        p.setId(1L);
        p.setName("TestProduct");
        p.setDescription("desc");
        p.setPrice(100.0);
        p.setStock(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        var result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("TestProduct", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }
}
