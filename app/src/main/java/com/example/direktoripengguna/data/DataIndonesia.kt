package com.example.direktoripengguna.data

object DataIndonesia {

    private val nama = listOf(
        "Budi Santoso",
        "Siti Rahmawati",
        "Agus Prasetyo",
        "Dewi Lestari",
        "Rizky Ramadhan",
        "Putri Anggraini",
        "Eko Wijaya",
        "Ayu Maharani",
        "Fajar Nugroho",
        "Rina Marlina"
    )

    private val username = listOf(
        "budisantoso",
        "sitirahma",
        "agusprast",
        "dewilestari",
        "rizkyram",
        "putrianggi",
        "ekowijaya",
        "ayumaharani",
        "fajarnug",
        "rinamarlina"
    )

    private val judul = listOf(
        "Pengalaman pertama naik KRL di pagi hari",
        "Resep nasi goreng kampung yang bikin nagih",
        "Tips hemat uang bulanan buat anak kos",
        "Cerita seru liburan ke pantai bareng keluarga",
        "Cara belajar coding dari nol tanpa pusing",
        "Warung kopi favorit untuk mengerjakan tugas",
        "Hujan deras dan macet di jam pulang kantor",
        "Belajar masak rendang untuk pertama kalinya",
        "Kenapa tidur cukup itu penting banget",
        "Review hape murah yang kameranya bagus",
        "Pengalaman daftar kuliah dan cari beasiswa",
        "Tips lolos wawancara kerja untuk fresh graduate",
        "Membuat kebun mini di halaman belakang rumah",
        "Resep es teh manis yang segar banget",
        "Serunya main futsal bareng teman kantor",
        "Rekomendasi film Indonesia yang wajib ditonton",
        "Cara merawat motor biar awet dan irit bensin",
        "Belajar bahasa Inggris lewat lagu dan film",
        "Catatan perjalanan naik gunung pertama kali",
        "Sarapan bubur ayam favorit di hari Minggu"
    )

    // Mengganti nama, username, dan email dengan versi Indonesia
    fun ubahKeIndonesia(user: User): User {
        val i = (user.id - 1).mod(nama.size)
        val namaBaru = nama[i]
        val usernameBaru = username[i]
        return user.copy(
            name = namaBaru,
            username = usernameBaru,
            email = "$usernameBaru@contoh.id"
        )
    }

    // Judul tulisan ke-"index" milik pengguna "userId"
    fun judulPostingan(userId: Int, index: Int): String {
        return judul[(userId * 3 + index).mod(judul.size)]
    }
}