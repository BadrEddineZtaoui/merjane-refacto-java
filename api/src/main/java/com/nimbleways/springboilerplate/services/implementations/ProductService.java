package com.nimbleways.springboilerplate.services.implementations;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    
    private final NotificationService notificationService;

    public ProductService(ProductRepository productRepository, NotificationService notificationService) {
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    public void notifyDelay(Product p) {
        notificationService.sendDelayNotification(p.getLeadTime(), p.getName());
    }

    public void processNormalProduct(Product product) {
        if (product.getAvailable() > 0) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            if (product.getLeadTime() > 0) {
                this.notifyDelay(product);
            }
        }
    }

    public void processSeasonalProduct(Product product) {
        // Add new season rules
        if ((LocalDate.now().isAfter(product.getSeasonStartDate()) && LocalDate.now().isBefore(product.getSeasonEndDate())
                && product.getAvailable() > 0)) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            this.handleSeasonalProduct(product);
        }
    }

    public void processExpirableProduct(Product product) {
        if (product.getAvailable() > 0 && product.getExpiryDate().isAfter(LocalDate.now())) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            this.handleExpiredProduct(product);
        }
    }

    public void processFlashSaleProduct(Product product) {
        if (product.getAvailable() > 0
                && (LocalDate.now().isAfter(product.getFlashSaleStartDate()) && LocalDate.now().isBefore(product.getFlashSaleEndDate()))
                && product.getFlashSaleSoldQuantity() < product.getFlashSaleMaxQuantity()
        ) {
            product.setAvailable(product.getAvailable() - 1);
            product.setFlashSaleSoldQuantity(product.getFlashSaleSoldQuantity() + 1);
            productRepository.save(product);
        }
    }

    private void handleSeasonalProduct(Product p) {
        if (LocalDate.now().plusDays(p.getLeadTime()).isAfter(p.getSeasonEndDate())) {
            notificationService.sendOutOfStockNotification(p.getName());
            p.setAvailable(0);
            productRepository.save(p);
        } else if (p.getSeasonStartDate().isAfter(LocalDate.now())) {
            notificationService.sendOutOfStockNotification(p.getName());
        } else {
            notifyDelay(p);
        }
    }

    private void handleExpiredProduct(Product p) {
        notificationService.sendExpirationNotification(p.getName(), p.getExpiryDate());
        p.setAvailable(0);
        productRepository.save(p);
    }
    
}