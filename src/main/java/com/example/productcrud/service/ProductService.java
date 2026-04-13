package com.example.productcrud.service;

import com.example.productcrud.model.Product;
import com.example.productcrud.Repository.Productrepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final Productrepository productRepository;

    private final List<Product> products = new ArrayList<>();
    private Long nextId = 7L;

    public ProductService(Productrepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}