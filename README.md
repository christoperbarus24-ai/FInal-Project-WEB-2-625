# Product CRUD — Praktikum JPA Fundamentals 2026

Aplikasi manajemen produk berbasis **Spring Boot + PostgreSQL** dengan fitur autentikasi, CRUD produk, pencarian, filter kategori, dan pagination.

---

## Teknologi yang Digunakan

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Security** — autentikasi berbasis form login
- **Spring Data JPA + Hibernate** — ORM dan akses database
- **PostgreSQL** — database relasional
- **Thymeleaf** — template engine untuk server-side rendering
- **Bootstrap 5.3** — tampilan antarmuka
- **Gradle** — build tool

---

## Fitur Aplikasi

| Fitur | Keterangan |
|---|---|
| Register & Login | Registrasi akun baru, login dengan Spring Security |
| Auth Guard | Redirect ke `/products` jika sudah login dan mengakses `/login` atau `/register` |
| CRUD Produk | Tambah, lihat detail, edit, dan hapus produk |
| Search | Pencarian nama produk (partial match, case-insensitive) via JPA Specification |
| Filter Kategori | Filter produk berdasarkan enum Category |
| Pagination | 10 produk per halaman menggunakan Spring Data `Pageable` |
| Dashboard | Statistik total produk, nilai inventory, produk aktif/tidak aktif, produk per kategori, dan low stock alert |
| Profil User | Lihat, edit profil, dan ganti password |

---

## Struktur Project

```
src/main/java/com/example/productcrud/
├── config/
│   ├── AuthPageFilter.java          # Filter redirect jika sudah login
│   └── SecurityConfig.java          # Konfigurasi Spring Security
├── controller/
│   ├── AuthController.java          # Register & Login
│   ├── DashboardController.java     # Halaman dashboard statistik
│   ├── ProductController.java       # CRUD + Search + Filter + Pagination
│   └── ProfileController.java       # Profil & ganti password
├── dto/
│   ├── ChangePasswordRequest.java
│   ├── EditProfileRequest.java
│   └── RegisterRequest.java
├── model/
│   ├── Category.java                # Enum kategori produk
│   ├── Product.java                 # Entity produk
│   └── User.java                    # Entity user
├── Repository/
│   ├── Productrepository.java       # JpaRepository + JpaSpecificationExecutor
│   ├── Productspecification.java    # JPA Criteria API untuk search & filter
│   └── UserRepository.java
└── service/
    ├── CustomUserDetailsService.java
    ├── ProductService.java           # Logic CRUD + searchAndFilter dengan Pageable
    └── UserService.java

src/main/resources/
├── templates/
│   ├── fragments/layout.html        # Navbar & footer fragment
│   ├── product/
│   │   ├── list.html                # Daftar produk + search + filter + pagination
│   │   ├── form.html                # Form tambah/edit produk
│   │   └── detail.html             # Detail produk
│   ├── dashboard.html
│   ├── login.html
│   ├── register.html
│   ├── profile.html
│   ├── edit-profile.html
│   └── change-password.html
└── application.properties
```

---

## Cara Menjalankan

### 1. Clone Repository

```bash
git clone https://github.com/<username>/<repo-name>.git
cd <repo-name>
```

### 2. Konfigurasi Environment Variable

Salin file `.env.example` menjadi `.env` dan isi dengan kredensial database PostgreSQL kamu:

```bash
cp .env.example .env
```

Edit file `.env`:

```
DATABASE_URL=jdbc:postgresql://<host>:<port>/<dbname>
PGHOST=<host>
PGUSER=<username>
PGDATABASE=<dbname>
PGPASSWORD=<password>
```

### 3. Konfigurasi `application.properties`

Pastikan `src/main/resources/application.properties` mengarah ke database yang benar:

```properties
spring.datasource.url=jdbc:postgresql://<host>:<port>/<dbname>
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Jalankan Aplikasi

```bash
./gradlew bootRun
```

Aplikasi akan berjalan di: [http://localhost:8080](http://localhost:8080)

> **Catatan:** Jika terjadi error `products_category_check constraint violation`, jalankan SQL berikut di database:
> ```sql
> ALTER TABLE products DROP CONSTRAINT products_category_check;
> ALTER TABLE products ADD CONSTRAINT products_category_check
> CHECK (category IN ('ELEKTRONIK','BUKU','MAKANAN','PAKAIAN','MINUMAN','KENDARAAN'));
> ```

---

## Penjelasan Fitur Pagination

Pagination diimplementasikan menggunakan **Spring Data JPA Pageable** dan **JPA Specification**:

- `ProductService.searchAndFilter()` menerima `keyword`, `category`, dan `page` (0-based)
- Menggunakan `PageRequest.of(page, 10, Sort.by("id").ascending())` — 10 produk per halaman
- `Productspecification.nameContains()` dan `categoryEquals()` membentuk query dinamis via Criteria API
- `ProductController` menerima parameter `?keyword=...&category=...&page=N` dari query string
- `list.html` menampilkan tombol Previous, nomor halaman, Next, serta info total data dan halaman aktif
- Search + Filter + Pagination bekerja bersamaan dan mempertahankan parameter saat pindah halaman

---

## Anggota Kelompok

| Nama | NIM |
|---|---|
| [Nama Anggota 1] | [NIM 1] |
| [Nama Anggota 2] | [NIM 2] |
| [Nama Anggota 3] | [NIM 3] |

---

## Lisensi

Project ini dibuat untuk keperluan praktikum. Dilarang digunakan untuk tujuan komersial.