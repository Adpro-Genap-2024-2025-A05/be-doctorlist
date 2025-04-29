# Deployment

Link: [BE-Profile Deployment](http://34.225.152.182/)

# Doctor Profile Service

Doctor Profile Service merupakan salah satu microservice dalam sistem PandaCare yang bertanggung jawab untuk menyediakan fitur pencarian dan penampilan profil dokter da. Layanan ini memungkinkan Pacillians (pengguna) untuk mencari  Caregiver (dokter) berdasarkan kriteria seperti nama, spesialisasi, atau jadwal kerja, serta melihat informasi detail dokter seperti alamat praktek, email, nomor telepon, dan rating.

Pada fitur "Mencari & Melihat Profil Dokter", digunakan Strategy Pattern. Karena pencarian dokter bisa dilakukan berdasarkan berbagai kriteria seperti nama, spesialisasi, atau jadwal kerja, maka Strategy Pattern memungkinkan tiap jenis pencarian di enkapsulasi dalam class terpisah. Hal ini membuat penambahan kriteria pencarian baru dapat dilakukan tanpa mengubah kode yang sudah ada. Dengan mendefinisikan sebuah antarmuka atau interface untuk strategi pencarian, saya hanya perlu membuat class konkret yang menangani logika pencarian berdasarkan suatu kriteria tertentu. Strategy pattern juga mendukung Open-Closed Principle. Prinsip ini jika nantinya saya ingin menambahkan kriteria pencarian baru semisal, pencarian berdasarkan lokasi atau rating maka hanya perlu menambahkan class strategy baru tanpa mengubah kode yang udah ada sebelumnya.

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