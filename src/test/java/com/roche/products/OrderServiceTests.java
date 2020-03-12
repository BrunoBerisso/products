package com.roche.products;

import com.roche.products.domain.OrderEntity;
import com.roche.products.domain.ProductReferenceEntity;
import com.roche.products.repository.OrderRepository;
import com.roche.products.repository.ProductRepository;
import com.roche.products.rest.dto.request.GetOrdersRequestDto;
import com.roche.products.rest.dto.response.OrderResponseDto;
import com.roche.products.rest.dto.response.ProductResponseDto;
import com.roche.products.rest.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTests {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    private OrderService orderService;

    private final LocalDateTime DISTANT_PAST = new Date(0L).toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDateTime();

    private final LocalDateTime NOW = LocalDateTime.now();

    private final LocalDateTime DISTANT_FUTURE = new Date(Long.MAX_VALUE).toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDateTime();

    @BeforeEach
    public void init() {
        orderService = new OrderService(orderRepository, productRepository);
    }

    @Test
    public void getOrders() {

        List<OrderEntity> oldestOrders = generateMockData().subList(0, 3);
        when(orderRepository.findByCreatedDateBetween(any(), any())).thenReturn(oldestOrders);

        GetOrdersRequestDto ordersFilter = GetOrdersRequestDto.builder()
                .fromDate(DISTANT_PAST)
                .toDate(NOW)
                .build();
        List<OrderResponseDto> filteredOrders = orderService.getOrders(ordersFilter);

        assertEquals(oldestOrders.size(), filteredOrders.size());
        assertEqualOrder(oldestOrders.get(0), filteredOrders.get(0));
    }

    @Test
    public void placeOrder() {
        //FIXME: finish implementing this
    }

    private void assertEqualOrder(OrderEntity expected, OrderResponseDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBuyerEmail(), actual.getBuyerEmail());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
        assertEquals(expected.getProducts().stream()
                .map(ProductReferenceEntity::getProductId).reduce(String::concat),
                actual.getProducts().stream()
                .map(ProductResponseDto::getSku).reduce(String::concat));
    }

    private List<OrderEntity> generateMockData() {
        return IntStream.range(1, 10)
                .mapToObj(i -> OrderEntity.builder()
                        .id("order_id_" + i)
                        .buyerEmail("buyer@email.com_" + i)
                        .createdDate(getMockDate(i))
                        .products(IntStream.range(1, 3)
                                .mapToObj(j -> ProductReferenceEntity.builder()
                                    .price(new BigDecimal(i + j))
                                    .productId("sku_ref_" + j)
                                    .id("prod_ref_id_" + (i + j))
                                    .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private LocalDateTime getMockDate(int index) {
        if (index <= 3) {
            return DISTANT_PAST;
        }
        if (index <= 6) {
            return NOW;
        } else { //(index <= 9)
            return DISTANT_FUTURE;
        }
    }
}
