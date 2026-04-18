package com.example.productcrud.controller;

import com.example.productcrud.model.Product;
import com.example.productcrud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Product> allProducts = productService.findAll(); // ✅ pakai findAll()

        // Total produk
        long totalProduk = allProducts.size();

        // Total nilai inventory (price * stock)
        double totalNilaiInventory = allProducts.stream()
                .mapToDouble(p -> p.getPrice() * p.getStock())
                .sum();

        // Jumlah produk aktif dan tidak aktif
        long totalAktif = allProducts.stream()
                .filter(Product::isActive)
                .count();
        long totalTidakAktif = totalProduk - totalAktif;

        // Jumlah produk per kategori (Category adalah enum, pakai getDisplayName())
        Map<String, Long> produkPerKategori = allProducts.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategory() != null
                                ? p.getCategory().getDisplayName() // ✅ pakai getDisplayName()
                                : "Tanpa Kategori",
                        Collectors.counting()
                ));

        // Daftar low stock (stock < 5)
        List<Product> lowStockProducts = allProducts.stream()
                .filter(p -> p.getStock() < 5)
                .collect(Collectors.toList());

        model.addAttribute("totalProduk", totalProduk);
        model.addAttribute("totalNilaiInventory", totalNilaiInventory);
        model.addAttribute("totalAktif", totalAktif);
        model.addAttribute("totalTidakAktif", totalTidakAktif);
        model.addAttribute("produkPerKategori", produkPerKategori);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("isEmpty", allProducts.isEmpty());

        return "dashboard";
    }
}
