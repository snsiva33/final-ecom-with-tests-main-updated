package com.example.ecommerce.service;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock ProductRepository repo;
    @InjectMocks ProductService service;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void create_and_get() {
        Product p = Product.builder().id(1L).name("X").price(10.0).stock(5).build();
        when(repo.save(any())).thenReturn(p);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Product created = service.create(p);
        assertEquals("X", created.getName());

        Product fetched = service.getById(1L);
        assertEquals(1L, fetched.getId());
    }

    @Test
    void get_not_found() {
        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(2L));
    }

    @Test
    void reduceStock_insufficient() {
        Product p = Product.builder().id(3L).stock(1).price(10.0).build();
        when(repo.findById(3L)).thenReturn(Optional.of(p));
        assertThrows(IllegalArgumentException.class, () -> service.reduceStock(3L, 2));
    }
}
