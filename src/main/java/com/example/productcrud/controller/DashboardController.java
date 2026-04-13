package com.example.productcrud.controller;

import com.example.productcrud.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalProduk = dashboardService.getTotalProduk();

        model.addAttribute("totalProduk", totalProduk);
        model.addAttribute("totalNilaiInventory", dashboardService.getTotalNilaiInventory());
        model.addAttribute("totalAktif", dashboardService.getTotalProdukAktif());
        model.addAttribute("totalTidakAktif", dashboardService.getTotalProdukTidakAktif());
        model.addAttribute("produkPerKategori", dashboardService.getProdukPerKategori());
        model.addAttribute("lowStockList", dashboardService.getLowStockProducts());
        model.addAttribute("adaProduk", totalProduk > 0);

        return "dashboard";
    }
}