package com.example.productcrud.Repository;

import com.example.productcrud.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Productrepository extends JpaRepository<Product, Long> {
}