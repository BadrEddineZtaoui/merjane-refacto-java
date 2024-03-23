package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductService productService;
    
    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }
    
    public ProcessOrderResponse processOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Set<Product> products = order.get().getItems();
            products.forEach(product -> {
                switch (product.getType()) {
                    case "NORMAL" -> productService.processNormalProduct(product);
                    case "SEASONAL" -> productService.processSeasonalProduct(product);
                    case "EXPIRABLE" -> productService.processExpirableProduct(product);
                    case "FLASHSALE" -> productService.processFlashSaleProduct(product);
                    default -> throw new IllegalArgumentException("Type de produit introuvable");
                }
            });

            return new ProcessOrderResponse(order.get().getId());
        }
        return null;
    }

}
