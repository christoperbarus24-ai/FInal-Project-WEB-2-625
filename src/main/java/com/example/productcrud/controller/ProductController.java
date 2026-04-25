package com.example.productcrud.controller;

import com.example.productcrud.model.Category;
import com.example.productcrud.model.Product;
import com.example.productcrud.service.CategoryService;
import com.example.productcrud.service.ProductService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String listProducts(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            Authentication authentication,
            Model model) {

        int pageIndex = Math.max(0, page - 1);
        Page<Product> productPage = productService.searchAndFilter(keyword, categoryId, pageIndex);
        List<Category> userCategories = productService.findCategoriesByUsername(authentication.getName());

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber() + 1);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("pageSize", productPage.getSize());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("categories", userCategories);

        return "product/list";
    }

    @GetMapping("/products/new")
    public String showCreateForm(Authentication authentication, Model model) {
        Product product = new Product();
        product.setCreatedAt(LocalDate.now());
        model.addAttribute("product", product);
        model.addAttribute("categories", productService.findCategoriesByUsername(authentication.getName()));
        return "product/form";
    }

    @GetMapping("/products/{id}")
    public String detailProduct(@PathVariable Long id, Model model) {
        return productService.findById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product/detail";
                })
                .orElse("redirect:/products");
    }

    // POST /products/save -> hanya untuk produk BARU (id == null)
    @PostMapping("/products/save")
    public String saveProduct(
            @ModelAttribute Product product,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (categoryId != null) {
            categoryService.findByIdAndUsername(categoryId, authentication.getName())
                    .ifPresent(product::setCategory);
        } else {
            product.setCategory(null);
        }

        productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil disimpan!");
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String showEditForm(@PathVariable Long id, Authentication authentication, Model model) {
        return productService.findById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("categories", productService.findCategoriesByUsername(authentication.getName()));
                    return "product/form";
                })
                .orElse("redirect:/products");
    }

    // FIX: Tambah endpoint POST untuk update produk yang sudah ada
    @PostMapping("/products/{id}/update")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product product,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        product.setId(id);

        if (categoryId != null) {
            categoryService.findByIdAndUsername(categoryId, authentication.getName())
                    .ifPresent(product::setCategory);
        } else {
            product.setCategory(null);
        }

        productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil diperbarui!");
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil dihapus!");
        return "redirect:/products";
    }
}