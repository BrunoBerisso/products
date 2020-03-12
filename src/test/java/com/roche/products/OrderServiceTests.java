package com.roche.products;

import com.roche.products.repository.OrderRepository;
import com.roche.products.repository.ProductRepository;
import com.roche.products.rest.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class OrderServiceTests {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    private OrderService orderService;

    @BeforeEach
    public void init() {
        orderService = new OrderService(orderRepository, productRepository);
    }
}
