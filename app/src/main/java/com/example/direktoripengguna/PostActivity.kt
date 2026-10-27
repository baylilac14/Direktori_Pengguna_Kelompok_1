package com.example.direktoripengguna

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.direktoripengguna.data.DataIndonesia
import com.example.direktoripengguna.data.PostBaru
import com.example.direktoripengguna.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PostActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_ID = "user_id"
        const val EXTRA_USER_NAME = "user_name"
    }

    private var userId = 0

    private lateinit var etJudul: EditText
    private lateinit var btnTambah: Button
    private lateinit var tvStatus: TextView
    private lateinit var listPost: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        findViewById<TextView>(R.id.tvNamaPengguna).text =
            intent.getStringExtra(EXTRA_USER_NAME) ?: "Tanpa nama"

        etJudul = findViewById(R.id.etJudul)
        btnTambah = findViewById(R.id.btnTambah)
        tvStatus = findViewById(R.id.tvStatus)
        listPost = findViewById(R.id.listPost)

        btnTambah.setOnClickListener { kirimPost() }

        muatPosts()
    }

    private fun muatPosts() {
        tvStatus.text = "Sebentar ya, sedang mengambil tulisan..."

        lifecycleScope.launch {
            try {
                val posts = RetrofitClient.api.getPostsByUser(userId)
                val data = posts.mapIndexed { index, _ ->
                    mapOf(
                        "nomor" to "Tulisan ke-${index + 1}",
                        "judul" to DataIndonesia.judulPostingan(userId, index)
                    )
                }
                listPost.adapter = SimpleAdapter(
                    this@PostActivity,
                    data,
                    R.layout.item_postingan,
                    arrayOf("nomor", "judul"),
                    intArrayOf(R.id.tvNomor, R.id.tvJudulPost)
                )
                tvStatus.text = "${posts.size} tulisan"
            } catch (e: HttpException) {
                tvStatus.text = "Server sedang bermasalah (kode ${e.code()})"
            } catch (e: IOException) {
                tvStatus.text = "Internet tidak tersambung"
            }
        }
    }

    private fun kirimPost() {
        val judul = etJudul.text.toString().trim()
        if (judul.isEmpty()) {
            etJudul.error = "Judul belum diisi"
            return
        }

        btnTambah.isEnabled = false
        lifecycleScope.launch {
            try {
                val baru = PostBaru(
                    title = judul,
                    body = "Ditulis lewat aplikasi Buku Kontak",
                    userId = userId
                )
                val hasil = RetrofitClient.api.buatPost(baru)
                tvStatus.text = "Tulisan terkirim! Nomornya ${hasil.id}"
                etJudul.text.clear()
            } catch (e: HttpException) {
                tvStatus.text = "Gagal mengirim (kode ${e.code()})"
            } catch (e: IOException) {
                tvStatus.text = "Internet tidak tersambung"
            } finally {
                btnTambah.isEnabled = true
            }
        }
    }
}