package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@UnitTest
public class ProductUnitTests {

    @Mock
    private NotificationService notificationService;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks 
    private ProductService productService;

    @Test
    public void testNormalProduct() {
        // GIVEN
        Product product = Product.builder()
                .leadTime(15)
                .available(0)
                .type("NORMAL")
                .name("RJ45 Cable")
                .build();


        Mockito.when(productRepository.save(product)).thenReturn(product);

        // WHEN
        productService.notifyDelay(product);

        // THEN
        assertEquals(0, product.getAvailable());
        assertEquals(15, product.getLeadTime());
        Mockito.verify(productRepository, Mockito.times(0)).save(product);
        Mockito.verify(notificationService, Mockito.times(1)).sendDelayNotification(product.getLeadTime(), product.getName());
    }

    @Test
    public void testFlashSaleProductSuccess() {
        // GIVEN
        Product product = Product.builder()
                .available(50)
                .type("FLASHSALE")
                .name("RJ45 Cable")
                .flashSaleMaxQuantity(20)
                .flashSaleSoldQuantity(10)
                .flashSaleStartDate(LocalDate.now().minusDays(1))
                .flashSaleEndDate(LocalDate.now().plusDays(1))
                .build();

        Product resultProduct = Product.builder()
                .available(49)
                .type("FLASHSALE")
                .name("RJ45 Cable")
                .flashSaleMaxQuantity(20)
                .flashSaleSoldQuantity(11)
                .flashSaleStartDate(LocalDate.now().minusDays(1))
                .flashSaleEndDate(LocalDate.now().plusDays(1))
                .build();

        Mockito.when(productRepository.save(product)).thenReturn(resultProduct);

        // WHEN
        productService.processFlashSaleProduct(product);

        // THEN
        assertEquals(49, resultProduct.getAvailable());
        assertEquals(11, resultProduct.getFlashSaleSoldQuantity());
        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }

    @Test
    public void testFlashSaleProductAvailable0() {
        // GIVEN
        Product product = Product.builder()
                .available(0)
                .type("FLASHSALE")
                .name("RJ45 Cable")
                .flashSaleMaxQuantity(20)
                .flashSaleSoldQuantity(10)
                .flashSaleStartDate(LocalDate.now().minusDays(1))
                .flashSaleEndDate(LocalDate.now().plusDays(1))
                .build();

        Mockito.when(productRepository.save(product)).thenReturn(product);

        // WHEN
        productService.processFlashSaleProduct(product);

        // THEN
        Mockito.verify(productRepository, Mockito.times(0)).save(product);
    }

    @Test
    public void testFlashSaleProductOutOfFlashSalesPeriod() {
        // GIVEN
        Product product = Product.builder()
                .available(50)
                .type("FLASHSALE")
                .name("RJ45 Cable")
                .flashSaleMaxQuantity(20)
                .flashSaleSoldQuantity(10)
                .flashSaleStartDate(LocalDate.now().minusDays(3))
                .flashSaleEndDate(LocalDate.now().minusDays(2))
                .build();

        Mockito.when(productRepository.save(product)).thenReturn(product);

        // WHEN
        productService.processFlashSaleProduct(product);

        // THEN
        Mockito.verify(productRepository, Mockito.times(0)).save(product);
    }

    @Test
    public void testFlashSaleProductIsMaxSoldQuantity() {
        // GIVEN
        Product product = Product.builder()
                .available(50)
                .type("FLASHSALE")
                .name("RJ45 Cable")
                .flashSaleMaxQuantity(20)
                .flashSaleSoldQuantity(20)
                .flashSaleStartDate(LocalDate.now().minusDays(1))
                .flashSaleEndDate(LocalDate.now().plusDays(1))
                .build();

        Mockito.when(productRepository.save(product)).thenReturn(product);

        // WHEN
        productService.processFlashSaleProduct(product);

        // THEN
        Mockito.verify(productRepository, Mockito.times(0)).save(product);
    }


}