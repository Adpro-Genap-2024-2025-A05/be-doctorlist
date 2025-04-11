# BE-Profile

Branch: `profile`

Created by: Syarna Savitri (2206083565)

## Deskripsi
Branch ini berisi implementasi fitur terkait pengelolaan profil pengguna dalam aplikasi backend BE-Profile. Fitur ini mencakup:
- Mendapatkan data profil pengguna.
- Memperbarui data profil pengguna.
- Menghapus akun pengguna.

## Struktur Proyek
- `src/main/java:` Berisi kode sumber utama aplikasi.
- `src/test/java:` Berisi pengujian unit dan integrasi.
- `src/main/resources:` Berisi file konfigurasi aplikasi.

## Design Pattern yang Digunakan
### 1. Service Layer Pattern
- Lokasi Implementasi: `ProfileService` dan `SecurityService`

- Tujuan:
Memisahkan logika bisnis dari lapisan controller untuk meningkatkan modularitas dan mempermudah pengujian.

- Contoh: `ProfileService` menangani logika pengelolaan profil pengguna, seperti mendapatkan, memperbarui, dan menghapus profil.

## 2. Repository Pattern

- Lokasi Implementasi: `ProfileRepository` dan
`UserRepository`

- Tujuan:
Mengabstraksi akses ke data dan memisahkan logika penyimpanan data dari logika bisnis.

- Contoh: `ProfileRepository` digunakan untuk mengakses data profil pengguna dari database.

## 3. Strategy Pattern
- Lokasi Implementasi: `SecurityService` dan `SecurityServiceImpl`

- Tujuan:
Memungkinkan implementasi strategi otentikasi dan otorisasi yang berbeda tanpa mengubah kode klien.

- Contoh: `SecurityServiceImpl` mengimplementasikan strategi otentikasi token dan otorisasi pengguna.

## Deployment
Aplikasi ini dapat diakses melalui tautan berikut: [BE-ProfileDeployment](https://gay-maurizia-be-profile-94f7c399.koyeb.app)