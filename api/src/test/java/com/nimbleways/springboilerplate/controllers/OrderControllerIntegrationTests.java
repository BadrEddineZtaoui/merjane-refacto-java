package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.utils.Annotations.SetupDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;

// import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.*;

// Specify the controller class you want to test
// This indicates to spring boot to only load UsersController into the context
// Which allows a better performance and needs to do less mocks
@SetupDatabase
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTests {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private NotificationService notificationService;

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private ProductRepository productRepository;

        @Test
        public void processOrderShouldReturn() throws Exception {
                List<Product> allProducts = createProducts();
                List<Product> resultProducts = resultProducts();
                Set<Product> orderItems = new HashSet<Product>(allProducts);
                Order order = createOrder(orderItems);
                productRepository.saveAll(allProducts);
                order = orderRepository.save(order);
                mockMvc.perform(get("/orders/{orderId}/processOrder", order.getId())
                                .contentType("application/json"))
                                .andExpect(status().isOk());
                Order resultOrder = orderRepository.findById(order.getId()).get();

                assertEquals(resultOrder.getId(), order.getId());
                assertEquals(orderItems.size(), resultOrder.getItems().size());
                resultProducts.forEach(product -> {
                    Product resultProduct = resultOrder.getItems().stream().filter(p -> Objects.equals(p.getName(), product.getName())).findFirst().orElse(null);
                    assert resultProduct != null;
                    assertEquals(product.getAvailable(), resultProduct.getAvailable());
                });
        }

        private static Order createOrder(Set<Product> products) {
                Order order = new Order();
                order.setItems(products);
                return order;
        }

        private static List<Product> createProducts() {
                List<Product> products = new ArrayList<>();
                products.add(
                    Product.builder()
                            .leadTime(15)
                            .available(30)
                            .type("NORMAL")
                            .name("USB Cable")
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(10)
                            .available(0)
                            .type("NORMAL")
                            .name("USB Dongle")
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(15)
                            .available(30)
                            .type("EXPIRABLE")
                            .name("Butter")
                            .expiryDate(LocalDate.now().plusDays(26))
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(90)
                            .available(6)
                            .type("EXPIRABLE")
                            .name("Milk")
                            .expiryDate(LocalDate.now().minusDays(2))
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(15)
                            .available(30)
                            .type("SEASONAL")
                            .name("Watermelon")
                            .seasonStartDate(LocalDate.now().minusDays(2))
                            .seasonEndDate(LocalDate.now().plusDays(58))
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(15)
                            .available(30)
                            .type("SEASONAL")
                            .name("Grapes")
                            .seasonStartDate(LocalDate.now().plusDays(180))
                            .seasonEndDate(LocalDate.now().plusDays(240))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(50)
                            .type("FLASHSALE")
                            .name("RJ45 Cable")
                            .flashSaleMaxQuantity(20)
                            .flashSaleSoldQuantity(10)
                            .flashSaleStartDate(LocalDate.now().minusDays(3))
                            .flashSaleEndDate(LocalDate.now().plusDays(3))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(50)
                            .type("FLASHSALE")
                            .name("Clavier")
                            .flashSaleMaxQuantity(20)
                            .flashSaleSoldQuantity(18)
                            .flashSaleStartDate(LocalDate.now().minusDays(3))
                            .flashSaleEndDate(LocalDate.now().plusDays(3))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(0)
                            .type("FLASHSALE")
                            .name("Souris")
                            .flashSaleMaxQuantity(20)
                            .flashSaleSoldQuantity(18)
                            .flashSaleStartDate(LocalDate.now().minusDays(3))
                            .flashSaleEndDate(LocalDate.now().plusDays(3))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(30)
                            .type("FLASHSALE")
                            .name("Ecran")
                            .flashSaleMaxQuantity(15)
                            .flashSaleSoldQuantity(15)
                            .flashSaleStartDate(LocalDate.now().minusDays(3))
                            .flashSaleEndDate(LocalDate.now().plusDays(3))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(30)
                            .type("FLASHSALE")
                            .name("USB")
                            .flashSaleMaxQuantity(10)
                            .flashSaleSoldQuantity(5)
                            .flashSaleStartDate(LocalDate.now().minusDays(10))
                            .flashSaleEndDate(LocalDate.now().minusDays(3))
                            .build()
                );
                return products;
        }

        private static List<Product> resultProducts() {
                List<Product> products = new ArrayList<>();
                products.add(
                    Product.builder()
                            .leadTime(15)
                            .available(29)
                            .type("NORMAL")
                            .name("USB Cable")
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(10)
                            .available(0)
                            .type("NORMAL")
                            .name("USB Dongle")
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(15)
                            .available(29)
                            .type("EXPIRABLE")
                            .name("Butter")
                            .expiryDate(LocalDate.now().plusDays(26))
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(90)
                            .available(0)
                            .type("EXPIRABLE")
                            .name("Milk")
                            .expiryDate(LocalDate.now().minusDays(2))
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(15)
                            .available(29)
                            .type("SEASONAL")
                            .name("Watermelon")
                            .seasonStartDate(LocalDate.now().minusDays(2))
                            .seasonEndDate(LocalDate.now().plusDays(58))
                            .build()
                );
                products.add(
                    Product.builder()
                            .leadTime(15)
                            .available(30)
                            .type("SEASONAL")
                            .name("Grapes")
                            .seasonStartDate(LocalDate.now().plusDays(180))
                            .seasonEndDate(LocalDate.now().plusDays(240))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(49)
                            .type("FLASHSALE")
                            .name("RJ45 Cable")
                            .flashSaleMaxQuantity(20)
                            .flashSaleSoldQuantity(11)
                            .flashSaleStartDate(LocalDate.now().minusDays(3))
                            .flashSaleEndDate(LocalDate.now().plusDays(3))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(49)
                            .type("FLASHSALE")
                            .name("Clavier")
                            .flashSaleMaxQuantity(20)
                            .flashSaleSoldQuantity(19)
                            .flashSaleStartDate(LocalDate.now().minusDays(3))
                            .flashSaleEndDate(LocalDate.now().plusDays(3))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(0)
                            .type("FLASHSALE")
                            .name("Souris")
                            .flashSaleMaxQuantity(20)
                            .flashSaleSoldQuantity(18)
                            .flashSaleStartDate(LocalDate.now().minusDays(3))
                            .flashSaleEndDate(LocalDate.now().plusDays(3))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(30)
                            .type("FLASHSALE")
                            .name("Ecran")
                            .flashSaleMaxQuantity(15)
                            .flashSaleSoldQuantity(15)
                            .flashSaleStartDate(LocalDate.now().minusDays(3))
                            .flashSaleEndDate(LocalDate.now().plusDays(3))
                            .build()
                );
                products.add(
                    Product.builder()
                            .available(30)
                            .type("FLASHSALE")
                            .name("USB")
                            .flashSaleMaxQuantity(10)
                            .flashSaleSoldQuantity(5)
                            .flashSaleStartDate(LocalDate.now().minusDays(10))
                            .flashSaleEndDate(LocalDate.now().minusDays(3))
                            .build()
                );
                return products;
        }
}
