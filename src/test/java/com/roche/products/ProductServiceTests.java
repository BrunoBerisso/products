package com.roche.products;

import com.roche.products.domain.ProductEntity;
import com.roche.products.repository.ProductRepository;
import com.roche.products.rest.dto.ProductDto;
import com.roche.products.rest.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTests {

    @MockBean
    private ProductRepository productRepositoryMock;

    private ProductService productService;

    @BeforeEach
    public void init() {
        productService = new ProductService(productRepositoryMock);
    }

    @Test
    public void getAllTest() {
        List<ProductEntity> allProducts = generateMockData();
        when(productRepositoryMock.findAllNotDeleted()).thenReturn(allProducts);

        ProductEntity expected = allProducts.get(allProducts.size() - 1);
        ProductDto anyProduct = productService.getAll().get(allProducts.size() - 1);
        assertEqualProduct(expected, anyProduct);
    }

    @Test
    public void addProductTest() {
        ProductEntity newProduct = generateMockData().get(0);
        when(productRepositoryMock.insert((ProductEntity) any())).thenReturn(newProduct);

        ProductDto result = productService.addProduct(newProduct.getName(), newProduct.getPrice());
        assertEqualProduct(newProduct, result);
    }

    private List<ProductEntity> generateMockData() {
        return IntStream.range(1, 10)
                .mapToObj(i -> ProductEntity.builder()
                        .id("just_added_sku_" + i)
                        .name("John Due - " + i)
                        .price(new BigDecimal(i))
                        .createdDate(LocalDateTime.now().minusSeconds(i))
                        .isDeleted(i % 2 == 0)
                        .build())
                .collect(Collectors.toList());
    }

    private void assertEqualProduct(ProductEntity expected, ProductDto actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getId(), actual.getSku());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
    }
}
