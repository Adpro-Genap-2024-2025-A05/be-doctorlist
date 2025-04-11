# Deployment

Link: [BE-Profile Deployment](https://gay-maurizia-be-profile-94f7c399.koyeb.app/)
# Doctor Profile Service

Doctor Profile Service merupakan salah satu microservice dalam sistem PandaCare yang bertanggung jawab untuk menyediakan fitur pencarian dan penampilan profil dokter da. Layanan ini memungkinkan Pacillians (pengguna) untuk mencari  Caregiver (dokter) berdasarkan kriteria seperti nama, spesialisasi, atau jadwal kerja, serta melihat informasi detail dokter seperti alamat praktek, email, nomor telepon, dan rating.

Pada fitur "Mencari & Melihat Profil Dokter", digunakan Strategy Pattern. Karena pencarian dokter bisa dilakukan berdasarkan berbagai kriteria seperti nama, spesialisasi, atau jadwal kerja, maka Strategy Pattern memungkinkan tiap jenis pencarian di enkapsulasi dalam class terpisah. Hal ini membuat penambahan kriteria pencarian baru dapat dilakukan tanpa mengubah kode yang sudah ada. Dengan mendefinisikan sebuah antarmuka atau interface untuk strategi pencarian, saya hanya perlu membuat class konkret yang menangani logika pencarian berdasarkan suatu kriteria tertentu. Strategy pattern juga mendukung Open-Closed Principle. Prinsip ini jika nantinya saya ingin menambahkan kriteria pencarian baru semisal, pencarian berdasarkan lokasi atau rating maka hanya perlu menambahkan class strategy baru tanpa mengubah kode yang udah ada sebelumnya. 

