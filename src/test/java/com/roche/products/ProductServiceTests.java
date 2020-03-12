package com.roche.products;

import com.roche.products.domain.ProductEntity;
import com.roche.products.repository.ProductRepository;
import com.roche.products.rest.dto.response.ProductResponseDto;
import com.roche.products.rest.dto.request.UpdateProductRequestDto;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
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
        when(productRepositoryMock.findByIsDeletedFalse()).thenReturn(allProducts);

        ProductEntity expected = allProducts.get(allProducts.size() - 1);
        ProductResponseDto anyProduct = productService.getAll().get(allProducts.size() - 1);
        assertEqualProduct(expected, anyProduct);
    }

    @Test
    public void addProductTest() {
        ProductEntity newProduct = generateMockData().get(0);
        when(productRepositoryMock.insert((ProductEntity) any())).thenReturn(newProduct);

        ProductResponseDto result = productService.addProduct(newProduct.getName(), newProduct.getPrice());
        assertEqualProduct(newProduct, result);
    }

    @Test
    public void updateProductTest() {
        String newName = "New Name";
        BigDecimal newPrice = new BigDecimal(1234);

        ProductEntity newProduct = generateMockData().get(0);
        newProduct.setName(newName);
        newProduct.setPrice(newPrice);

        when(productRepositoryMock.findById(eq(newProduct.getId()))).thenReturn(java.util.Optional.of(newProduct));
        when(productRepositoryMock.save(any())).thenReturn(newProduct);

        UpdateProductRequestDto requestDto = UpdateProductRequestDto.builder()
                .name(newName)
                .price(newPrice)
                .build();
        ProductResponseDto result = productService.updateProduct(newProduct.getId(), requestDto);
        assertEqualProduct(newProduct, result);
    }

    @Test
    public void deleteProductTest() {
        ProductEntity newProduct = generateMockData().get(0);

        when(productRepositoryMock.findById(eq(newProduct.getId()))).thenReturn(java.util.Optional.of(newProduct));
        when(productRepositoryMock.save(any())).thenReturn(newProduct);

        ProductResponseDto result = productService.deleteProduct(newProduct.getId());
        assertTrue(newProduct.getIsDeleted());
        assertEqualProduct(newProduct, result);
    }

    private List<ProductEntity> generateMockData() {
        return IntStream.range(1, 5)
                .mapToObj(i -> ProductEntity.builder()
                        .id("just_added_sku_" + i)
                        .name("John Due - " + i)
                        .price(new BigDecimal(i))
                        .createdDate(LocalDateTime.now().minusSeconds(i))
                        .isDeleted(i % 2 == 0)
                        .build())
                .collect(Collectors.toList());
    }

    private void assertEqualProduct(ProductEntity expected, ProductResponseDto actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getId(), actual.getSku());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
    }
}
