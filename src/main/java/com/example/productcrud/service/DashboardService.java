package com.example.productcrud.service;

import com.example.productcrud.model.Product;
import com.example.productcrud.Repository.Productrepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final Productrepository productRepository;

    public DashboardService(Productrepository productRepository) {
        this.productRepository = productRepository;
    }

    // Total jumlah semua produk
    public long getTotalProduk() {
        return productRepository.count();
    }

    // Total nilai inventory = sum(price * stock)
    public double getTotalNilaiInventory() {
        return productRepository.findAll().stream()
                .mapToDouble(p -> p.getPrice() * p.getStock())
                .sum();
    }

    // Jumlah produk aktif
    public long getTotalProdukAktif() {
        return productRepository.findAll().stream()
                .filter(Product::isActive)
                .count();
    }

    // Jumlah produk tidak aktif
    public long getTotalProdukTidakAktif() {
        return productRepository.findAll().stream()
                .filter(p -> !p.isActive())
                .count();
    }

    // Jumlah produk per kategori (Category enum -> nama string)
    public Map<String, Long> getProdukPerKategori() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        p -> (p.getCategory() != null ? p.getCategory().name() : "Tanpa Kategori"),
                        Collectors.counting()
                ));
    }

    // Daftar produk low stock (stock < 5)
    public List<Product> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getStock() < 5)
                .collect(Collectors.toList());
    }
}