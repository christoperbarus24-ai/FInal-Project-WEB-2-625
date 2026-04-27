package com.example.productcrud.dto;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import com.example.productcrud.model.Product;

public class DashboardStats {
    private long totalProduk;
    private BigDecimal totalNilaiInventory;
    private long jumlahAktif;
    private long jumlahTidakAktif;
    private Map<String, Long> produkPerKategori;
    private List<Product> lowStockList;

    public long getTotalProduk() { return totalProduk; }
    public void setTotalProduk(long totalProduk) { this.totalProduk = totalProduk; }

    public BigDecimal getTotalNilaiInventory() { return totalNilaiInventory; }
    public void setTotalNilaiInventory(BigDecimal totalNilaiInventory) { this.totalNilaiInventory = totalNilaiInventory; }

    public long getJumlahAktif() { return jumlahAktif; }
    public void setJumlahAktif(long jumlahAktif) { this.jumlahAktif = jumlahAktif; }

    public long getJumlahTidakAktif() { return jumlahTidakAktif; }
    public void setJumlahTidakAktif(long jumlahTidakAktif) { this.jumlahTidakAktif = jumlahTidakAktif; }

    public Map<String, Long> getProdukPerKategori() { return produkPerKategori; }
    public void setProdukPerKategori(Map<String, Long> produkPerKategori) { this.produkPerKategori = produkPerKategori; }

    public List<Product> getLowStockList() { return lowStockList; }
    public void setLowStockList(List<Product> lowStockList) { this.lowStockList = lowStockList; }
}