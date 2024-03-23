package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@UnitTest
public class OrderUnitTests {


    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;

    @Test
    public void testGetOrderById() {
        // GIVEN
        Order order = new Order(1L, new HashSet<>());

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // WHEN
        orderService.processOrder(1L);

        // THEN
        Mockito.verify(orderRepository, Mockito.times(1)).findById(1L);
    }
}
