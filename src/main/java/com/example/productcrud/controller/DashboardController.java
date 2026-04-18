package com.example.productcrud.controller;

import com.example.productcrud.model.Category;
import com.example.productcrud.model.Product;
import com.example.productcrud.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final ProductService productService;

    public DashboardController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Product> allProducts = productService.findAll();

        // Total produk
        int totalProduk = allProducts.size();

        // Total nilai inventory (harga x stok)
        long totalNilaiInventory = allProducts.stream()
                .mapToLong(p -> p.getPrice() * p.getStock())
                .sum();

        // Jumlah produk aktif vs tidak aktif
        long totalAktif = allProducts.stream()
                .filter(Product::isActive)
                .count();
        long totalTidakAktif = totalProduk - totalAktif;

        // Jumlah produk per kategori
        Map<String, Long> produkPerKategori = new LinkedHashMap<>();
        for (Category cat : Category.values()) {
            long count = allProducts.stream()
                    .filter(p -> p.getCategory() == cat)
                    .count();
            produkPerKategori.put(cat.getDisplayName(), count);
        }

        // Daftar low stock (stok < 5)
        List<Product> lowStockProducts = allProducts.stream()
                .filter(p -> p.getStock() < 5)
                .collect(Collectors.toList());

        model.addAttribute("totalProduk", totalProduk);
        model.addAttribute("totalNilaiInventory", totalNilaiInventory);
        model.addAttribute("totalAktif", totalAktif);
        model.addAttribute("totalTidakAktif", totalTidakAktif);
        model.addAttribute("produkPerKategori", produkPerKategori);
        model.addAttribute("lowStockProducts", lowStockProducts);

        return "dashboard";
    }
}