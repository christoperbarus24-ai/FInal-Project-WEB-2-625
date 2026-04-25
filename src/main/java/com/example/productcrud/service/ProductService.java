package com.example.productcrud.service;

import com.example.productcrud.Repository.CategoryRepository;
import com.example.productcrud.Repository.Productrepository;
import com.example.productcrud.Repository.Productspecification;
import com.example.productcrud.Repository.UserRepository;
import com.example.productcrud.model.Category;
import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final Productrepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // Jumlah produk per halaman
    private static final int PAGE_SIZE = 5;

    public ProductService(Productrepository productRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // Dipakai oleh DashboardController yang butuh semua data tanpa pagination
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

    /**
     * Mencari produk dengan dukungan:
     * - Keyword: partial match, case-insensitive pada nama produk
     * - Category: filter berdasarkan id Category entity (null = semua kategori)
     * - Pagination: PAGE_SIZE produk per halaman, diurutkan berdasarkan id ascending
     *
     * @param keyword    kata kunci pencarian (boleh null/kosong)
     * @param categoryId id category entity untuk filter (boleh null = semua)
     * @param page       nomor halaman (0-based dari Spring, tapi UI kirim 1-based)
     * @return Page<Product> berisi data produk dan info pagination
     */
    public Page<Product> searchAndFilter(String keyword, Long categoryId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").ascending());

        Specification<Product> spec = Productspecification.nameContains(keyword)
                .and(Productspecification.categoryIdEquals(categoryId));

        return productRepository.findAll(spec, pageable);
    }

    /**
     * Ambil category milik user tertentu untuk ditampilkan di dropdown form produk.
     * Hanya category milik user yang login yang ditampilkan.
     */
    public List<Category> findCategoriesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan: " + username));
        return categoryRepository.findByUserIdOrderByNameAsc(user.getId());
    }
}